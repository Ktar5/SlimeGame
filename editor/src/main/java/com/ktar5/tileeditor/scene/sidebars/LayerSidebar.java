package com.ktar5.tileeditor.scene.sidebars;

import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.layers.BaseLayer;
import com.ktar5.tileeditor.util.KChangeListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;

@Getter
public class LayerSidebar extends VisTable {
    private final Tilemap tilemap;
    private final ListView<BaseLayer> listView;
    private final LayerAdapter adapter;

    public LayerSidebar(Tilemap tilemap) {
        this.tilemap = tilemap;
        adapter = new LayerAdapter(tilemap.getLayers().getLayers());
        listView = new ListView<>(adapter);
        add(listView.getMainTable()).grow();
    }

    public class LayerAdapter extends AbstractListAdapter<BaseLayer, VisTable> {
        private final ArrayList<BaseLayer> array;

        public LayerAdapter(ArrayList<BaseLayer> array) {
            this.array = array;
        }

        @Override
        public Iterable<BaseLayer> iterable() {
            return array;
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
            array.sort(comparator);
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
