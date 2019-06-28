//package com.ktar5.slime.screens.loading;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.assets.AssetDescriptor;
//import com.badlogic.gdx.assets.AssetLoaderParameters;
//import com.badlogic.gdx.assets.AssetManager;
//import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
//import com.badlogic.gdx.assets.loaders.FileHandleResolver;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.utils.Array;
//import com.ktar5.gameengine.tilemap.CustomTmxMapLoader;
//import com.ktar5.slime.world.level.LevelData;
//import com.ktar5.slime.world.level.LevelHandler;
//import org.tinylog.Logger;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class MapFileLoader extends AsynchronousAssetLoader<Map<Integer, FileHandle>, MapFileLoader.MapFileLoaderParameters> {
//    Map<Integer, FileHandle> mapFileMap;
//
//    public MapFileLoader(FileHandleResolver resolver) {
//        super(resolver);
//    }
//
//    @Override
//    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapFileLoaderParameters parameter) {
//        return null;
//    }
//
//    @Override
//    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MapFileLoaderParameters parameter) {
//        mapFileMap = new HashMap<>();
//
//        FileHandle levelAtlas = Gdx.files.internal("maps/levels.txt");
//        LevelHandler.LOAD_MAPS_LOCAL = false;
//        if (Gdx.files.local("maps/levels.txt").exists()) {
//            Logger.debug("Using local maps instead of internal.");
//            levelAtlas = Gdx.files.local("maps/levels.txt");
//            LevelHandler.LOAD_MAPS_LOCAL = true;
//        }
//
//        FileHandle handle;
//        BufferedReader bufferedReader = new BufferedReader(levelAtlas.reader());
//        String line;
//
//        try {
//            int i = 0;
//            while ((line = bufferedReader.readLine()) != null) {
//                String[] split = line.split(":");
//                if (split.length == 1 || split[1].isEmpty() || split[1].equals(" ")) {
//                    Logger.debug("Null level: " + i);
//                    mapFileMap.put(i, null);
//                    i++;
//                    continue;
//                }
//                String levelName = split[1].replace(" ", "");
//                if (LevelHandler.LOAD_MAPS_LOCAL) {
//                    handle = (Gdx.files.local("maps/" + levelName + ".tmx"));
//                } else {
//                    handle = (Gdx.files.internal("maps/" + levelName + ".tmx"));
//                }
//                mapFileMap.put(i, handle);
//                i++;
//            }
//        } catch (IOException e) {
//            Logger.debug("ERROR >> Couldn't load level file list!");
//            Logger.error(e);
//        }
//    }
//
//    @Override
//    public Map<Integer, FileHandle> loadSync(AssetManager manager, String fileName, FileHandle file, MapFileLoaderParameters parameter) {
//        Map<Integer, FileHandle> mapfiles = this.mapFileMap;
//        this.mapFileMap = null;
//        return mapfiles;
//    }
//
//    public void thuiig() {
//
//        try {
//            levelDataList.add(i, new LevelData(tiledMap, levelName, i));
//        } catch (Exception e) {
//            levelDataList.add(i, null);
//            Logger.debug("Could not load level data for level: " + i);
//            Logger.error(e);
//        }
//
//        //TODO
//        if (i == 0) {
////                    FileWriter fileWriter = new FileWriter("../assets/test.tmx");
////                    new CustomTmxMapWriter(fileWriter).tmx(tiledMap, CustomTmxMapWriter.Format.CSV);
////                    fileWriter.flush();
////                    fileWriter.close();
//        }
//
//
//        LevelData[] levelData = new LevelData[levelDataList.size()];
//        for (int i = 0; i < levelDataList.size(); i++) {
//            levelData[i] = levelDataList.get(i);
//        }
//
//
//
//    }
//
//
//    public void loadLevel(int id, FileHandle handle) {
//        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
//        params.textureMinFilter = Texture.TextureFilter.Linear;
//        params.textureMagFilter = Texture.TextureFilter.Nearest;
//        params.generateMipMaps = true;
//
//        CustomTmxMapLoader loader = new CustomTmxMapLoader();
//        TiledMap tiledMap;
//
//        Logger.debug("Loading map id: " + i + " name: " + handle.name());
//        tiledMap = loader.load("maps/" + handle.name(), params);
//    }
//
//    static public class MapFileLoaderParameters extends AssetLoaderParameters<Map<Integer, FileHandle>> {
//    }
//
//}
