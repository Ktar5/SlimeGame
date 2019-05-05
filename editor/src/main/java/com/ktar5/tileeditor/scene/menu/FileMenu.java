package com.ktar5.tileeditor.scene.menu;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.tilemap.MapManager;
import com.ktar5.tileeditor.tileset.TilesetManager;
import com.ktar5.tileeditor.util.KChangeListener;

public class FileMenu extends Menu {

    public FileMenu() {
        super("File");
        final MenuItem openMenuItem = new MenuItem("Open..");
        final PopupMenu openMenu = new PopupMenu();
        openMenuItem.setSubMenu(openMenu);

        final MenuItem openTilemap = new MenuItem("Open Tilemap", new KChangeListener((changeEvent, actor) -> {
            MapManager.get().loadMap();
        }));

        final MenuItem openTileset = new MenuItem("Open Tileset", new KChangeListener((changeEvent, actor) -> {
            TilesetManager.get().loadTileset(Tileset -> {
//                    EditorCoordinator.get().getEditor().setSelectedTab(baseTileset.getId());
            }, true);
        }));

        openMenu.addItem(openTilemap);
        openMenu.addItem(openTileset);


        final MenuItem newMap = new MenuItem("New Tilemap", new KChangeListener((changeEvent, actor) -> {
            MapManager.get().createMap();
        }));

        final MenuItem newTileset = new MenuItem("New Tileset", new KChangeListener((changeEvent, actor) -> {
            TilesetManager.get().createTileset();
        }));

        final MenuItem newMenuItem = new MenuItem("New..");
        final PopupMenu newMenu = new PopupMenu();
        newMenuItem.setSubMenu(newMenu);
        newMenu.addItem(newMap);
        newMenu.addItem(newTileset);

        final MenuItem save = new MenuItem("Save Current", new KChangeListener((changeEvent, actor) -> {
            if (Main.getInstance().getRoot().getTabHoldingPane().getActiveTab() != null)
                Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab().getTabbable().save();
        }));

        final MenuItem saveAs = new MenuItem("Save As..", new KChangeListener((changeEvent, actor) -> {
            if (Main.getInstance().getRoot().getTabHoldingPane().getActiveTab() != null)
                Main.getInstance().getRoot().getTabHoldingPane().getCurrentTab().getTabbable().saveAs();
        }));

        addItem(newMenuItem);

        addItem(openMenuItem);

        addItem(save);

        addItem(saveAs);

        addItem(new MenuItem("Open Recent"));

        addItem(new MenuItem("Revert"));
    }

}
