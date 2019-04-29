package com.ktar5.tileeditor.scene.topmenu;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.centerview.tabs.AbstractTab;
import com.ktar5.tileeditor.scene.centerview.tabs.TilemapTab;
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
                TilesetManager.get().loadTileset(wholeTileset -> {
                    Tilemap map = MapManager.get().getMap((currentTab).getTabId());
                    map.getTilesets().addTileset(wholeTileset);
                });
            }
        }));
        final MenuItem addLayer = new MenuItem("Add Layer", new KChangeListener((changeEvent, actor) -> {
            AbstractTab currentTab = Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab();
            if (currentTab instanceof TilemapTab) {
                Tilemap map = MapManager.get().getMap((currentTab).getTabId());
                Layers layers = map.getLayers();
                //TODO create dialog
                layers.getLayers().add(new TileLayer(map, "layer" + layers.getLayers().size(), true, 0, 0));
            }
        }));
        this.addItem(addTileset);
        this.addItem(addLayer);
    }


}
