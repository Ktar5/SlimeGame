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
import com.ktar5.gameengine.tilemap.CustomTmxMapLoader;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.gameengine.util.Updatable;
import com.ktar5.slime.SlimeGame;
import lombok.Getter;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

@Getter
public class LevelHandler implements Renderable, Updatable {
    public static boolean LOAD_MAPS_LOCAL = false;
    public static boolean SHOW_LEVEL_DEBUG = false;

    //A level data is the background information for storing the level
    //A loaded level is a level that is being played
    private LevelData[] levelData;

    private LoadedLevel currentLevel;
    //    private OrthoCachedTiledMapRenderer tileMapRenderer;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    public LevelHandler() {
        loadMaps();
        setLevel(0);
    }

    public void resetLevel() {
        currentLevel.reset();
    }

    public boolean isLevelNull(int levelIndex) {
        return levelData[levelIndex % levelData.length] == null;
    }

    public void setLevel(int levelIndex) {
        levelIndex = levelIndex % levelData.length;
        if (levelData[levelIndex] == null) {
            setLevel(levelIndex + 1);
            return;
        }
        if (currentLevel != null) {
            currentLevel.reset();
            if (levelIndex == currentLevel.getId()) {
                return;
            }
        }
        currentLevel = new LoadedLevel(levelData[levelIndex]);
//        tileMapRenderer = new OrthoCachedTiledMapRenderer(currentLevel.getRenderMap());
        tileMapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getRenderMap(), 1f,
                SlimeGame.getGame().getSpriteBatch());
        currentLevel.getRenderMap().getLayers().get("Gameplay").setVisible(SHOW_LEVEL_DEBUG);
    }

    public void advanceLevel() {
        setLevel((currentLevel.getId() + 1) % levelData.length);
    }

    public int getLevelCount() {
        return levelData.length;
    }

    private void loadMaps() {
        FileHandle levelAtlas = Gdx.files.internal("maps/levels.txt");
        LOAD_MAPS_LOCAL = false;
        if (Gdx.files.local("maps/levels.txt").exists()) {
            Logger.debug("Using local maps instead of internal.");
            levelAtlas = Gdx.files.local("maps/levels.txt");
            LOAD_MAPS_LOCAL = true;
        }

        FileHandle handle;
        ArrayList<LevelData> levelDataList = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(levelAtlas.reader());
        String line;
        try {
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String[] split = line.split(":");
                    if (split.length == 1 || split[1].isEmpty() || split[1].equals(" ")) {
                        Logger.debug("Null level: " + i);
                        levelDataList.add(i, null);
                        i++;
                        continue;
                    }
                    String levelName = split[1].replace(" ", "");
                    if (LOAD_MAPS_LOCAL) {
                        handle = (Gdx.files.local("maps/" + levelName + ".tmx"));
                    } else {
                        handle = (Gdx.files.internal("maps/" + levelName + ".tmx"));
                    }
                    TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
                    params.textureMinFilter = Texture.TextureFilter.Linear;
                    params.textureMagFilter = Texture.TextureFilter.Nearest;
                    params.generateMipMaps = true;

                    CustomTmxMapLoader loader = new CustomTmxMapLoader();
                    TiledMap tiledMap;

                    Logger.debug("Loading map id: " + i + " name: " + handle.name());
                    tiledMap = loader.load("maps/" + handle.name(), params);

                    //TODO
                    if (i == 0) {
//                    FileWriter fileWriter = new FileWriter("../assets/test.tmx");
//                    new CustomTmxMapWriter(fileWriter).tmx(tiledMap, CustomTmxMapWriter.Format.CSV);
//                    fileWriter.flush();
//                    fileWriter.close();
                    }

                    levelDataList.add(i, new LevelData(tiledMap, levelName, i));
                } catch (Exception e) {
                    Logger.debug("Could not load level data for level: " + i);
                    Logger.error(e);
                }
                i++;
            }
        } catch (IOException e) {
            Logger.debug("ERROR >> Couldn't load levelData!");
            Logger.error(e);
            return;
        }
        levelData = new LevelData[levelDataList.size()];
        for (int i = 0; i < levelDataList.size(); i++) {
            levelData[i] = levelDataList.get(i);
        }

    }


    public int getSpawnX() {
        return currentLevel.getSpawnTile().x;
    }

    public int getSpawnY() {
        return currentLevel.getSpawnTile().y;
    }

    @Override
    public void update(float dTime) {
        currentLevel.update(dTime);
    }


    @Override
    public void render(SpriteBatch batch, float dTime) {
        batch.end();
        tileMapRenderer.setView(SlimeGame.getGame().getGameCamera().getCamera());
        //Logger.debug(ToStringBuilder.reflectionToString(EngineManager.get().getCameraBase().getCamera(), ToStringStyle.JSON_STYLE));
        //Logger.debug("\n\n\n");
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

        if (SHOW_LEVEL_DEBUG) {
            SlimeGame.getGame().getShapeRenderer().setAutoShapeType(true);
            SlimeGame.getGame().getShapeRenderer().setProjectionMatrix(EngineManager.get().getCameraBase().getCamera().combined);
            SlimeGame.getGame().getShapeRenderer().begin();
            for (int x = 0; x < currentLevel.getGameMap().length; x++) {
                for (int y = 0; y < currentLevel.getGameMap()[x].length; y++) {
                    SlimeGame.getGame().getShapeRenderer().rect(x * 16f, y * 16f, 2, 2);
                }
            }
            debug(dTime);
            SlimeGame.getGame().getShapeRenderer().end();
        }

        batch.begin();
    }

    @Override
    public void debug(float dTime) {
        for (Entity entity : currentLevel.getEntities()) {
            entity.debugRender(Renderable.getShapeRenderer());
        }
        currentLevel.getPlayer().debugRender(Renderable.getShapeRenderer());
    }

    public void setLevelDebug(boolean debug) {
        SHOW_LEVEL_DEBUG = debug;
        currentLevel.getRenderMap().getLayers().get("Gameplay").setVisible(debug);
    }

    public void toggleDebug() {
        setLevelDebug(!SHOW_LEVEL_DEBUG);
    }
}
