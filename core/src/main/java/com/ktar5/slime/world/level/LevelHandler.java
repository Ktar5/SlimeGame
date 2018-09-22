package com.ktar5.slime.world.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.tilemap.CustomTmxMapLoader;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.variables.Constants;
import com.ktar5.utilities.common.util.CollectingFileScanner;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class LevelHandler implements Renderable, Updatable {
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
        tileMapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getTileMap(), Constants.MAP_SCALE,
                EngineManager.get().getRenderManager().getSpriteBatch());
    }

    public void advanceLevel(){
        setLevel((currentLevel.id + 1) % levels.length);
    }

    public int getLevelCount(){
        return levels.length;
    }

    private void loadMaps() {
        File mapsFolder = new File("../assets/maps/");
        List<File> fileList = new CollectingFileScanner(mapsFolder, (file) -> file.getName().endsWith(".tmx")).getFiles();
        levels = new Level[fileList.size()];

        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;

        CustomTmxMapLoader loader = new CustomTmxMapLoader();
        TiledMap tiledMap;

        for (File file : fileList) {
            int index = Integer.valueOf(
                    file.getName().replace(".tmx", "").replace("Level", "")
            );
            if(index > fileList.size()){
                throw new RuntimeException("Error loading level: " + index + ". Its index is too high.");
            }
            tiledMap = loader.load("maps/" + file.getName(), params);
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
        batch.end();
        tileMapRenderer.render(currentLevel.getForegroundLayers());
        //tileMapRenderer.render();
        batch.begin();
    }

    @Override
    public void debug(float dTime) {

    }
}
