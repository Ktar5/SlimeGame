package com.ktar5.slime.world.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.rendering.Renderable;
import com.ktar5.gameengine.tilemap.CustomTmxMapLoader;
import com.ktar5.gameengine.util.Updatable;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelHandler implements Renderable, Updatable {
    public static boolean LOAD_MAPS_LOCAL = true;

    private Level[] levels;
    private OrthogonalTiledMapRenderer tileMapRenderer;
    @Getter
    private LoadedLevel currentLevel;

    public LevelHandler() {
        loadMaps();
        setLevel(0);
    }

    public void resetLevel() {
        currentLevel.reset();
    }

    public void setLevel(int levelIndex) {
        if (currentLevel != null) {
            currentLevel.reset();
            if (levelIndex == currentLevel.getId()) {
                return;
            }
        }
        currentLevel = new LoadedLevel(levels[levelIndex]);
        tileMapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getTileMap(), 1f,
                EngineManager.get().getRenderManager().getSpriteBatch());
    }

    public void advanceLevel() {
        Logger.debug(new Throwable(), "Advanced Level");
        setLevel((currentLevel.id + 1) % levels.length);
    }

    public int getLevelCount() {
        return levels.length;
    }

    private void loadMaps() {
        List<FileHandle> fileList = new ArrayList<>();

        FileHandle levelAtlas = Gdx.files.internal("maps/levels.txt");
        LOAD_MAPS_LOCAL = false;
        if (Gdx.files.local("maps/levels.txt").exists()) {
            Logger.debug("Using local maps instead of internal.");
            levelAtlas = Gdx.files.local("maps/levels.txt");
            LOAD_MAPS_LOCAL = true;
        }

        BufferedReader bufferedReader = new BufferedReader(levelAtlas.reader());
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                String levelName = split[1].replace(" ", "");
                if (LOAD_MAPS_LOCAL) {
                    fileList.add(Gdx.files.local("maps/" + levelName + ".tmx"));
                } else {
                    fileList.add(Gdx.files.internal("maps/" + levelName + ".tmx"));
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR >> Couldn't load levels!");
            e.printStackTrace();
            return;
        }

        levels = new Level[fileList.size()];

        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;

        CustomTmxMapLoader loader = new CustomTmxMapLoader();
        TiledMap tiledMap;

        for (int i = 0; i < fileList.size(); i++) {
            tiledMap = loader.load("maps/" + fileList.get(i).name(), params);
            levels[i] = new Level(tiledMap, i);
        }
    }

    public int getSpawnX() {
        return currentLevel.getSpawnX();
    }

    public int getSpawnY() {
        return currentLevel.getSpawnY();
    }

    @Override
    public void update(float dTime) {
        currentLevel.update(dTime);
    }


    @Override
    public void render(SpriteBatch batch, float dTime) {
        batch.end();
        tileMapRenderer.setView(EngineManager.get().getCameraBase().getCamera());
        //System.out.println(ToStringBuilder.reflectionToString(EngineManager.get().getCameraBase().getCamera(), ToStringStyle.JSON_STYLE));
        //System.out.println("\n\n\n");
        tileMapRenderer.render(currentLevel.getBackgroundLayers());
        batch.begin();
        currentLevel.getPlayer().getEntityAnimator().render(batch, currentLevel.getPlayer().getPosition().x,
                currentLevel.getPlayer().getPosition().y, currentLevel.getPlayer().getPosition().getAngle());
        for (Entity entity : currentLevel.getEntities()) {
            entity.getEntityAnimator().render(batch, entity.getPosition().x, entity.getPosition().y, entity.getPosition().getAngle());
        }
        batch.end();
        tileMapRenderer.render(currentLevel.getForegroundLayers());
        //tileMapRenderer.render();
        batch.begin();
    }

    @Override
    public void debug(float dTime) {
        for (Entity entity : currentLevel.getEntities()) {
            entity.debugRender(EngineManager.get().getRenderManager().getShapeRenderer());
        }
    }
}
