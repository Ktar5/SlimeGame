package com.ktar5.tileeditor.scene.sidebars;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter;
import com.kotcrab.vis.ui.widget.*;
import com.ktar5.tileeditor.tilemap.Layers;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.layers.BaseLayer;
import com.ktar5.tileeditor.tilemap.layers.TileLayer;
import com.ktar5.tileeditor.util.KChangeListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Getter
public class LayerSidebar extends VisTable {
    private final Tilemap tilemap;
    private final ListView<BaseLayer> listView;
    private final LayerAdapter adapter;
    VisSlider slider;

    public LayerSidebar(Tilemap tilemap) {
        this.tilemap = tilemap;
        adapter = new LayerAdapter(tilemap.getLayers().getLayers());
        listView = new ListView<>(adapter);
        add(listView.getMainTable()).grow();

        VisTable bottomBar = new VisTable();
        VisTextButton moveLayerUp = new VisTextButton("^");
        moveLayerUp.addListener(new KChangeListener((changeEvent, actor) -> {
            Layers layers = tilemap.getLayers();
            if (layers.getLayers().size() == 1) {
                return;
            }
            Array<BaseLayer> selection = adapter.getSelectionManager().getSelection();
            if (selection.size != 1) {
                return;
            }
            BaseLayer layer = selection.get(0);
            int layerIndex = layers.idFromLayer(layer);
            if (layerIndex == layers.getLayers().size() - 1) {
                return;
            }
            layers.moveLayerUp(layerIndex);
            adapter.itemsChanged();
            adapter.getSelectionManager().select(layer);
        }));

        VisTextButton moveLayerDown = new VisTextButton("v");
        moveLayerDown.addListener(new KChangeListener((changeEvent, actor) -> {
            Layers layers = tilemap.getLayers();
            if (layers.getLayers().size() == 1) {
                return;
            }
            Array<BaseLayer> selection = adapter.getSelectionManager().getSelection();
            if (selection.size != 1) {
                return;
            }
            BaseLayer layer = selection.get(0);
            int layerIndex = layers.idFromLayer(layer);
            System.out.println(layerIndex);
            if (layerIndex == 0) {
                return;
            }
            layers.moveLayerDown(layerIndex);
            adapter.itemsChanged();
            adapter.getSelectionManager().select(layer);
        }));

        VisTextButton addLayer = new VisTextButton("+");
        addLayer.addListener(new KChangeListener((changeEvent, actor) -> {
            Layers layers = tilemap.getLayers();
            TileLayer tileLayer = new TileLayer(tilemap, "layer" + layers.getLayers().size(), true, 0, 0);
            layers.getLayers().add(tileLayer);
            adapter.itemsChanged();
            adapter.getSelectionManager().select(tileLayer);
        }));

        VisTextButton deleteButton = new VisTextButton("X");
        deleteButton.addListener(new KChangeListener((changeEvent, actor) -> {
            Layers layers = tilemap.getLayers();
            if (layers.getLayers().size() == 1) {
                return;
            }
            Array<BaseLayer> selection = adapter.getSelectionManager().getSelection();
            if (selection.size != 1) {
                return;
            }
            BaseLayer layer = selection.get(0);
            layers.remove(layer);
            adapter.itemsChanged();
            adapter.getSelectionManager().select(layers.getLayers().get(0));

        }));


        bottomBar.add(moveLayerUp, moveLayerDown, addLayer);

        slider = new VisSlider(0, 100, 2, false);
        slider.addListener(new KChangeListener((changeEvent, actor) -> {
            Layers layers = tilemap.getLayers();
            layers.getActiveLayer().setTransparency((int) slider.getValue());
        }));

        slider.setSnapToValues(new float[]{0f, 50f, 100f}, 3);

        bottomBar.add(slider).growX().pad(0, 15, 0, 15);
        bottomBar.add(deleteButton);
        row();
        add(bottomBar).growX();
    }

    public class LayerAdapter extends AbstractListAdapter<BaseLayer, VisTable> {
        private final ArrayList<BaseLayer> array;
        private final Drawable selection = VisUI.getSkin().getDrawable("list-selection");

        public LayerAdapter(ArrayList<BaseLayer> array) {
            this.array = array;
            setSelectionMode(SelectionMode.SINGLE);
            getSelectionManager().setListener(new ListSelectionListener<BaseLayer, VisTable>() {
                @Override
                public void selected(BaseLayer item, VisTable view) {
                    item.getParent().getLayers().setActiveLayerId(item.getParent().getLayers().idFromLayer(item));
                    if (slider != null) {
                        slider.setValue(item.getTransparency());
                    }
                }

                @Override
                public void deselected(BaseLayer item, VisTable view) {
                    //nothing
                }
            });
            if (array.size() > 0 && array.get(0) != null) {
                getSelectionManager().select(array.get(0));
            }
        }

        @Override
        public Iterable<BaseLayer> iterable() {
            ArrayList<BaseLayer> baseLayers = new ArrayList<>(array);
            Collections.reverse(baseLayers);
            return baseLayers;
        }

        @Override
        public int size() {
            return array.size();
        }

        @Override
        public int indexOf(BaseLayer item) {
            return array.indexOf(item);
        }

        @Override
        protected void selectView(VisTable view) {
            if (view == null) {
                return;
            }
            view.setBackground(selection);
        }

        @Override
        protected void deselectView(VisTable view) {
            if (view == null) {
                return;
            }
            view.setBackground((Drawable) null);
        }

        @Override
        public void add(BaseLayer element) {
            throw new RuntimeException("You shouldn't have done this.");
//            array.add(element);
//            itemAdded(element);
        }

        @Override
        public BaseLayer get(int index) {
            return array.get(index);
        }

        @Override
        protected void sort(Comparator<BaseLayer> comparator) {
//            array.sort(comparator);
        }

        @Override
        protected VisTable createView(BaseLayer item) {
            VisTable table = new VisTable();
            VisLabel visLabel = new VisLabel("   " + item.getName());
            table.add(visLabel).left().growX();
            VisTextButton visTextButton = new VisTextButton(item.isVisible() ? "V" : "H");
            visTextButton.addListener(new KChangeListener((changeEvent, actor) -> {
                item.setVisible(!item.isVisible());
                visTextButton.setText(item.isVisible() ? "V" : "H");
            }));
            table.add(visTextButton).right();
            return table;
        }
    }


}
