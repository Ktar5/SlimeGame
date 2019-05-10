package com.ktar5.slime.tools.tilemapprocessor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.ktar5.gameengine.tilemap.CustomTmxMapLoader;
import com.ktar5.utilities.common.util.CollectingFileScanner;

import java.io.File;
import java.util.List;

public class TiledMapProcessor {
    private static final TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
    private static final CustomTmxMapLoader loader = new CustomTmxMapLoader();

    public static void main (String[] args) {
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.forceExit = false;
        config.width = 100;
        config.height = 50;
        config.title = "TiledMapPacker";
        new LwjglApplication(new ApplicationListener() {
            @Override public void resume () { }
            @Override public void resize (int width, int height) { }
            @Override public void render () { }
            @Override public void pause () { }
            @Override public void dispose () { }
            @Override public void create () {
                File mapsFolder = new File("maps");
                List<File> fileList = new CollectingFileScanner(mapsFolder, (file) -> file.getName().endsWith(".tmx")).getFiles();

                for (File file : fileList) {
                    int index = Integer.valueOf(
                            file.getName().replace(".tmx", "").replace("LevelData", "")
                    );
                    if (index > fileList.size()) {
                        throw new RuntimeException("Error loading level: " + index + ". Its index is too high.");
                    }
                    TiledMap load = loader.load("maps/" + file.getName(), params);
                    new TileLevelData(load, index);
                }
                System.out.println("Finished processing.");
                Gdx.app.exit();
            }
        }, config);
    }

}
