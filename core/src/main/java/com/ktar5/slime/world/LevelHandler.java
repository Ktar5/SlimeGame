package com.ktar5.slime.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.variables.Constants;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;

import java.util.EnumMap;

public class LevelHandler implements Renderable, Updatable {
    private EnumMap<Levels, Level> levels;
    private OrthogonalTiledMapRenderer tileMapRenderer;
    @Getter
    private LoadedLevel currentLevel;
    
    public LevelHandler(Levels start) {
        loadMaps();
        setLevel(start);
    }
    
    public void setLevel(Levels level) {
        currentLevel = new LoadedLevel(levels.get(level));
        tileMapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getTileMap(), Constants.MAP_SCALE,
                EngineManager.get().getRenderManager().getSpriteBatch());
    }
    
    private void loadMaps() {
        levels = new EnumMap<>(Levels.class);
        
        AtlasTmxMapLoader.AtlasTiledMapLoaderParameters params = new AtlasTmxMapLoader.AtlasTiledMapLoaderParameters();
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;
        
        AtlasTmxMapLoader loader = new AtlasTmxMapLoader();
        TiledMap tiledMap;
        TiledMapTileLayer tiledMapTileLayer;
        
        for (Levels levelRef : Levels.values()) {
            tiledMap = loader.load("maps/generated/" + levelRef.path + ".tmx", params);
            
            tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
            tiledMapTileLayer.setOffsetX(-8);
            tiledMapTileLayer.setOffsetY(8);
            
            int x = (int) tiledMap.getProperties().get("startx");
            int y = tiledMapTileLayer.getWidth() - ((int) tiledMap.getProperties().get("starty"));
            
            levels.put(levelRef, new Level(Pair.of(x, y), tiledMap, levelRef));
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
