package com.ktar5.tileeditor.tileset;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.centerview.tabs.TilesetTab;
import com.ktar5.tileeditor.scene.dialogs.CreateWholeTileset;
import com.ktar5.tileeditor.scene.dialogs.GenericAlert;
import com.ktar5.tileeditor.tilemap.whole.WholeTileset;
import com.ktar5.tileeditor.util.StringUtil;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class TilesetManager {
    private static TilesetManager instance;
    private HashMap<UUID, BaseTileset> tilesetHashMap;

    public TilesetManager() {
        this.tilesetHashMap = new HashMap<>();
    }

    /**
     * Gets the instance of the TilesetManager
     */
    public static TilesetManager get() {
        if (instance == null) {
            instance = new TilesetManager();
        }
        return instance;
    }

    /**
     * Removes the tileset with the given id
     */
    public void remove(UUID uuid) {
        if (this.tilesetHashMap.containsKey(uuid)) {
            Logger.debug("Removed tileset: " + getTileset(uuid).getSaveFile().getName());
            tilesetHashMap.remove(uuid);
        }
    }

    /**
     * Return the tileset (if it exists) with the given id
     */
    public BaseTileset getTileset(UUID id) {
        if (!tilesetHashMap.containsKey(id)) {
            throw new RuntimeException("BaseTileset with id: " + id + " doesn't exist");
        }
        return tilesetHashMap.get(id);
    }

    public void createTileset() {
        CreateWholeTileset.create(this::createTileset);
    }

    /**
     * Create a tileset of the type specified. Uses a create dialog.
     *
     * @return the tileset of type <T> that has been instantiated, otherwise null
     */
    public WholeTileset createTileset(CreateWholeTileset createDialog) {
        if (createDialog == null) {
            new GenericAlert("Something went wrong during the process of creating the tileset, please try again.");
            return null;
        }

        File tilesetFile = createDialog.getTilesetFile().file();
        for (BaseTileset tileset1 : tilesetHashMap.values()) {
            if (tileset1.getSaveFile().getPath().equals(tilesetFile.getPath())) {
                new GenericAlert("Tileset with path " + tilesetFile.getAbsolutePath() + " already loaded.\n" +
                        "Please close tab for " + tilesetFile.getName() + " then try creating new tileset again.");
                return null;
            }
        }

        WholeTileset tileset = new WholeTileset(createDialog.getSourceFile().file(), createDialog.getTilesetFile().file(),
                createDialog.getPaddingVertical(), createDialog.getPaddingHorizontal(),
                createDialog.getOffsetLeft(), createDialog.getOffsetUp(),
                createDialog.getTileWidth(), createDialog.getTileHeight());

        tilesetHashMap.put(tileset.getId(), tileset);
        TilesetTab tab;
        Main.getInstance().getRoot().getTabHoldingPane().addTab(tab = new TilesetTab(tileset.getId()));
        return tileset;
    }

    //TODO Optimize
    public BaseTileset getOrLoadTileset(File loaderFile) {
        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return null;
        }

        WholeTileset tileset = new WholeTileset(loaderFile, new JSONObject(data));

        for (BaseTileset temp : tilesetHashMap.values()) {
            if (temp.getSaveFile().getPath().equals(tileset.getSaveFile().getPath())) {
                return temp;
            }
        }
        return loadTileset(loaderFile);
    }

    /**
     * Load a tileset of the type specified from the file specified.
     *
     * @return the tileset of type <T> that has been instantiated, otherwise null
     */
    public WholeTileset loadTileset(File loaderFile) {
        Logger.info("Beginning to load tileset from file: " + loaderFile.getPath());

        String data = StringUtil.readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return null;
        }

        WholeTileset tileset = new WholeTileset(loaderFile, new JSONObject(data));

        for (BaseTileset temp : tilesetHashMap.values()) {
            if (temp.getSaveFile().getPath().equals(tileset.getSaveFile().getPath())) {
                new GenericAlert("Tileset with path " + tileset.getSaveFile().getAbsolutePath() + " already loaded");
            }
        }

        tilesetHashMap.put(tileset.getId(), tileset);
        TilesetTab tilesetTab = new TilesetTab(tileset.getId());
        Main.getInstance().getRoot().getTabHoldingPane().addTab(tilesetTab);
        Logger.info("Finished loading tileset: " + tileset.getSaveFile().getName());
        return tileset;
    }

    /**
     * Loads a tileset from a file selected in an "open file" dialog, and instantiates it using
     * the serialization constructor of tileset.
     */
    public void loadTileset(Consumer<WholeTileset> consumer) {
        FileChooser fileChooser = Main.getInstance().getRoot().getFileChooser();
        fileChooser.setMode(com.kotcrab.vis.ui.widget.file.FileChooser.Mode.OPEN);
        fileChooser.setSelectionMode(com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode.FILES);
        fileChooser.setName("Load Tileset");
        FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
        fileTypeFilter.addRule("Tileset File", "json");
        fileChooser.setFileTypeFilter(fileTypeFilter);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                FileHandle file = files.get(0);
                if (file == null) {
                    Logger.info("Tried to load tileset, cancelled or failed");
                } else if (!file.exists()) {
                    new GenericAlert("The selected file: " + file.path() + " does not exist. Try again.");
                }
                consumer.accept(loadTileset(file.file()));
                fileChooser.fadeOut();
            }
        });

        Main.getInstance().getRoot().addActor(fileChooser.fadeIn());
    }

    /**
     * Save a tileset with the UUID specified.
     *
     * @param id the uuid of the tileset to be saved.
     */
    public void saveTileset(UUID id) {
        Logger.info("Starting save for baseTileset (" + id + ")");

        if (!tilesetHashMap.containsKey(id)) {
            Logger.info("BaseTileset not loaded so could not be saved id: (" + id + ")");
            return;
        }

        BaseTileset baseTileset = tilesetHashMap.get(id);

        if (baseTileset.getSaveFile().exists()) {
            baseTileset.getSaveFile().delete();
        }

        try {
            baseTileset.getSaveFile().createNewFile();
            FileWriter writer = new FileWriter(baseTileset.getSaveFile());
            writer.write(baseTileset.serialize().toString(4));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("An error occured during save");
            return;
        }
        Main.getInstance().getRoot().getTabHoldingPane().getTab(baseTileset.getId()).setEdit(false);
        Main.getInstance().getRoot().getTabHoldingPane().getTab(baseTileset.getId()).setDirty(false);
        Logger.info("Finished save for baseTileset (" + id + ") in " + "\"" + baseTileset.getSaveFile() + "\"");
    }


}
