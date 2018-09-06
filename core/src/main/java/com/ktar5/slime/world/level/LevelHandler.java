package com.ktar5.slime.world.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.variables.Constants;
import com.ktar5.slime.world.CustomTmxMapLoader;
import lombok.Getter;

import java.util.EnumMap;

public class LevelHandler implements Renderable, Updatable {
    private EnumMap<LevelRef, Level> levels;
    private OrthogonalTiledMapRenderer tileMapRenderer;
    @Getter
    private LoadedLevel currentLevel;

    public LevelHandler() {
        loadMaps();
        setLevel(LevelRef.values()[0]);
    }

    public void resetLevel() {
        currentLevel.reset();
    }

    public void setLevel(LevelRef levelRef) {
        currentLevel = new LoadedLevel(levels.get(levelRef));
        tileMapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getTileMap(), Constants.MAP_SCALE,
                EngineManager.get().getRenderManager().getSpriteBatch());
    }

    private void loadMaps() {
        levels = new EnumMap<>(LevelRef.class);

        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;

        CustomTmxMapLoader loader = new CustomTmxMapLoader();
        TiledMap tiledMap;

        for (LevelRef levelRef : LevelRef.values()) {
            tiledMap = loader.load("maps/nongen/" + levelRef.path + ".tmx", params);
            levels.put(levelRef, new Level(tiledMap, levelRef));
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
        tileMapRenderer.render();
        batch.begin();
        currentLevel.getPlayer().getEntityAnimator().render(batch, currentLevel.getPlayer().getPosition().x,
                currentLevel.getPlayer().getPosition().y, currentLevel.getPlayer().getPosition().getAngle());
    }

    @Override
    public void debug(float dTime) {

    }
}
