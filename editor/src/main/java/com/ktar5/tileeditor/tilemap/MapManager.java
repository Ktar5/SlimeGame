package com.ktar5.tileeditor.tilemap;

import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.dialogs.CreateTilemap;
import com.ktar5.tileeditor.scene.dialogs.GenericAlert;
import com.ktar5.tileeditor.scene.dialogs.LoadDialog;
import com.ktar5.tileeditor.scene.tabs.TilemapTab;
import com.ktar5.tileeditor.tilemap.layers.TileLayer;
import com.ktar5.tileeditor.util.StringUtil;
import org.json.JSONObject;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MapManager {
    private static MapManager instance;
    private HashMap<UUID, Tilemap> openMaps;
    private ArrayList<Class<? extends Tilemap>> registeredMapTypes;

    public MapManager() {
        instance = this;
        registeredMapTypes = new ArrayList<>();
        openMaps = new HashMap<>();

        //Initialize tinylog
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .level(Level.DEBUG)
                .addWriter(new org.pmw.tinylog.writers.FileWriter("log.txt"))
                .formatPattern("{date:mm:ss:SSS} {class_name}.{method}() [{level}]: {message}")
                .activate();

    }

    /**
     * Gets the instance of the MapManager
     */
    public static MapManager get() {
        if (instance == null) {
            instance = new MapManager();
//            throw new RuntimeException("Please initialize tile manager first.");
        }
        return instance;
    }

    /**
     * Removes the tilemap with the given id
     */
    public void remove(UUID uuid) {
        if (this.openMaps.containsKey(uuid)) {
            Logger.debug("Removed tilemap: " + getMap(uuid).getName());
            openMaps.remove(uuid);
        }
    }

    /**
     * Return the map (if it exists) with the id given
     */
    public Tilemap getMap(UUID id) {
        if (!openMaps.containsKey(id)) {
            throw new RuntimeException("Tilemap with id: " + id + " doesn't exist");
        }
        return openMaps.get(id);
    }

    public void createMap() {
        CreateTilemap.create(this::createMap);
    }

    /**
     * Create a tilemap of the type specified. Uses a create dialog.
     *
     * @return the tilemap of type <T> that has been instantiated, otherwise null
     */
    public void createMap(CreateTilemap createTilemap) {
        if (createTilemap == null) {
            new GenericAlert("Something went wrong during the process of creating the map, please try again.");
            return;
        }

        File file = createTilemap.getFile().file();
        for (Tilemap tilemap1 : openMaps.values()) {
            if (tilemap1.getSaveFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                new GenericAlert("Tilemap with path " + file.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + file.getName() + " then try creating new map again.");
                return;
            }
        }

        Tilemap tilemap = new Tilemap(createTilemap.getFile().file(),
                createTilemap.getWidth(),
                createTilemap.getHeight(),
                createTilemap.getTileWidth(),
                createTilemap.getTileHeight());
        TileLayer tileLayer = new TileLayer(tilemap, "Tile Layer", true, 0, 0);
        tilemap.getLayers().getLayers().add(tileLayer);
        openMaps.put(tilemap.getId(), tilemap);
        TilemapTab tilemapTab = new TilemapTab(tilemap);
        tilemapTab.setDirty(true);
        Main.getInstance().getRoot().getTabHoldingPane().addTab(tilemapTab);
    }


    public void loadMap() {
        LoadDialog.create("Load a tilemap", "Json Tilemap File",
                "json", fileHandle -> loadMap(fileHandle.file()));
    }

    /**
     * Loads a tilemap from a file selected in an "open file" dialog, and instantiates it using
     * the serialization constructor of tilemap.
     *
     * @return the tilemap of type <T> that has been instantiated, otherwise null
     */
    public void loadMap(File loaderFile) {
        if (loaderFile == null) {
            Logger.info("Tried to load map, cancelled or failed");
            return;
        } else if (!loaderFile.exists()) {
            new GenericAlert("The selected file: " + loaderFile.getPath() + " does not exist. Try again.");
            return;
        }

        Logger.info("Beginning to load map from file: " + loaderFile.getPath());

        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            Logger.error("Data from file: " + loaderFile.getPath() + " is either null or empty.");
            return;
        }

        Tilemap tilemap = new Tilemap(loaderFile, new JSONObject(data));

        for (Tilemap temp : openMaps.values()) {
            if (temp.getSaveFile().getPath().equals(tilemap.getSaveFile().getPath())) {
                new GenericAlert("Tilemap with path " + tilemap.getSaveFile().getAbsolutePath() + " already loaded");
            }
        }

        openMaps.put(tilemap.getId(), tilemap);
        Main.getInstance().getRoot().getTabHoldingPane().addTab(new TilemapTab(tilemap));
        Logger.info("Finished loading map: " + tilemap.getName());
    }

    /**
     * Save a map with the UUID specified.
     *
     * @param id the uuid of the map to be saved.
     */
    public void saveMap(UUID id) {
        Logger.info("Starting save for tilemap (" + id + ")");

        if (!openMaps.containsKey(id)) {
            Logger.info("Map not loaded so could not be saved id: (" + id + ")");
            return;
        }

        Tilemap tilemap = openMaps.get(id);
        if (tilemap.getSaveFile().exists()) {
            tilemap.getSaveFile().delete();
        }

        try {
            tilemap.getSaveFile().createNewFile();
            FileWriter writer = new FileWriter(tilemap.getSaveFile());
            writer.write(tilemap.serialize().toString(4));
            Main.getInstance().getRoot().getTabHoldingPane().getTab(id).setDirty(false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Logger.info("Finished save for tilemap (" + id + ") in " + "\"" + tilemap.getSaveFile() + "\"");
    }

}
