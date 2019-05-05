package com.ktar5.tileeditor.scene.menu;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.tabs.AbstractTab;
import com.ktar5.tileeditor.scene.tabs.TilemapTab;
import com.ktar5.tileeditor.tilemap.Layers;
import com.ktar5.tileeditor.tilemap.MapManager;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.layers.TileLayer;
import com.ktar5.tileeditor.tileset.TilesetManager;
import com.ktar5.tileeditor.util.KChangeListener;

public class MapMenu extends Menu {

    public MapMenu() {
        super("Map");

        final MenuItem addTileset = new MenuItem("Add Tileset", new KChangeListener((changeEvent, actor) -> {
            AbstractTab currentTab = Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab();
            if (currentTab instanceof TilemapTab) {
                TilesetManager.get().loadTileset(Tileset -> {
                    Tilemap map = MapManager.get().getMap((currentTab).getTabId());
                    map.getTilesets().addTileset(Tileset);
                }, false);
                currentTab.setDirty(true);
            }
        }));
        final MenuItem addLayer = new MenuItem("Add Layer", new KChangeListener((changeEvent, actor) -> {
            AbstractTab currentTab = Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab();
            if (currentTab instanceof TilemapTab) {
                TilemapTab mapTab = (TilemapTab) currentTab;
                Tilemap map = MapManager.get().getMap((currentTab).getTabId());
                Layers layers = map.getLayers();
                //TODO create dialog
                TileLayer tileLayer = new TileLayer(map, "layer" + layers.getLayers().size(), true, 0, 0);
                layers.getLayers().add(tileLayer);
                mapTab.getLayerSidebar().getAdapter().itemsChanged();
                mapTab.getLayerSidebar().getAdapter().getSelectionManager().select(tileLayer);
                mapTab.setDirty(true);
            }
        }));
        this.addItem(addTileset);
        this.addItem(addLayer);
    }


}
