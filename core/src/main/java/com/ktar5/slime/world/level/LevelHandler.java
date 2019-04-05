package com.ktar5.slime.world.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.tilemap.CustomTmxMapLoader;
import com.ktar5.slime.engine.util.Updatable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LevelHandler implements Renderable, Updatable {
    public static final boolean LOAD_MAPS_LOCAL = false;

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
        currentLevel = new LoadedLevel(levels[levelIndex]);
        tileMapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getTileMap(), 1f,
                EngineManager.get().getRenderManager().getSpriteBatch());
    }

    public void advanceLevel() {
        setLevel((currentLevel.id + 1) % levels.length);
    }

    public int getLevelCount() {
        return levels.length;
    }

    private void loadMaps() {
        List<FileHandle> fileList = new ArrayList<>();
        int i = 0;
        FileHandle handle = Gdx.files.internal("maps/Level0.tmx");
        if (LOAD_MAPS_LOCAL) {
            handle = Gdx.files.local("maps/Level0.tmx");
        }
        while (handle.exists()) {
            fileList.add(handle);
            i++;
            if (LOAD_MAPS_LOCAL) {
                System.out.println("Load local");
                handle = Gdx.files.local("maps/Level" + i + ".tmx");
            } else {
                handle = Gdx.files.internal("maps/Level" + i + ".tmx");
            }

        }
        levels = new Level[fileList.size()];

        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;

        CustomTmxMapLoader loader = new CustomTmxMapLoader();
        TiledMap tiledMap;

        for (FileHandle file : fileList) {
            int index = Integer.valueOf(
                    file.name().replace(".tmx", "").replace("Level", "")
            );
            if (index > fileList.size()) {
                throw new RuntimeException("Error loading level: " + index + ". Its index is too high.");
            }
            tiledMap = loader.load("maps/" + file.name(), params);
            levels[index] = new Level(tiledMap, index);
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
