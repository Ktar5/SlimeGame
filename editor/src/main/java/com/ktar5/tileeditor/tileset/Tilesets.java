package com.ktar5.tileeditor.tileset;

import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.dialogs.GenericAlert;
import com.ktar5.tileeditor.scene.sidebars.tileset.MapTilesetTab;
import com.ktar5.tileeditor.scene.tabs.TilemapTab;
import com.ktar5.tileeditor.tilemap.Tilemap;
import lombok.Getter;
import org.json.JSONArray;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tilesets extends ArrayList<Tileset> {
    private Tilemap parent;
    @Getter
    private ArrayList<Tileset> tilesets;
    private Map<String, Integer> tilesetNameMap;

    public Tilesets(Tilemap parent) {
        this.parent = parent;
        tilesets = new ArrayList<>();
        tilesetNameMap = new HashMap<>();
    }

    public void loadTilesets(JSONArray json) {
        for (int i = 0; i < json.length(); i++) {
            loadTileset(json.getString(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("tilesets: ");
        int i = 0;
        for (Tileset tileset : tilesets) {
            stringBuilder.append(i).append(": ").append(tileset.getName());
            i++;
        }
        return stringBuilder.toString();
    }

    /**
     * Loads a tileset from the path, or retrieves it if it is already loaded
     *
     * @param path the relative path to the tileset file
     */
    private void loadTileset(String path) {
        Logger.debug("Loading tileset: " + path);
        File file = Paths.get(parent.getSaveFile().getPath()).resolve(path).toFile();
        Tileset tileset = TilesetManager.get().getOrLoadTileset(file, false);
        Logger.debug("Does tileset: " + path + " equal null?: " + (tileset == null));
        if (tileset.getTileHeight() != parent.getTileHeight() || tileset.getTileWidth() != parent.getTileWidth()) {
            new GenericAlert("Tileset's tilesize does not match map's tilesize");
            return;
        }
        tilesets.add(tileset);
        tilesetNameMap.put(tileset.getName().toLowerCase(), tilesets.size() - 1);
    }

    public JSONArray serialize() {
        JSONArray json = new JSONArray();
        for (int i = 0; i < tilesets.size(); i++) {
            String path = Paths
                    .get(parent.getSaveFile().getPath())
                    .relativize(Paths.get(tilesets.get(i).getSaveFile().getPath()))
                    .toString();
            json.put(i, path);
        }
        return json;
    }

    public void addTileset(Tileset Tileset) {
        if (tilesets.contains(Tileset)) {
            return;
        }
        tilesets.add(Tileset);
        tilesetNameMap.put(Tileset.getName().toLowerCase(), tilesets.size() - 1);
        TilemapTab tab = (TilemapTab) Main.getInstance().getRoot().getTabHoldingPane().getTab(parent.getId());
        tab.getTilesetSidebar().getTabHoldingPane().addTab(new MapTilesetTab(Tileset, tab.getTilesetSidebar()));
    }

    public void removeTileset(Tileset Tileset) {
        tilesets.remove(Tileset);
        tilesetNameMap.remove(Tileset.getName().toLowerCase());
    }

    public void removeTileset(int index) {
        if (index >= tilesets.size()) {
            new GenericAlert("Cannot remove tileset id greater than size: " + index);
            return;
        }
        Tileset remove = tilesets.remove(index);
        tilesetNameMap.remove(remove.getName().toLowerCase());
    }

    public Tileset getTileset(String name) {
        if (tilesetNameMap.containsKey(name.toLowerCase())) {
            return tilesets.get(tilesetNameMap.get(name.toLowerCase()));
        }
        return null;
    }

    public int getIndexByTileset(Tileset tileset) {
        return tilesetNameMap.get(tileset.getName().toLowerCase());
    }

    public Tileset getTileset(int index) {
        if (index >= tilesets.size()) {
            new GenericAlert("Cannot remove tileset id greater than size: " + index);
            return null;
        }
        return tilesets.get(index);
    }

}
