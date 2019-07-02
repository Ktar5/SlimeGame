package com.ktar5.slime.world.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.IntMap;
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
import java.util.List;

@Getter
public class LevelHandler implements Renderable, Updatable {
    public static boolean LOAD_MAPS_LOCAL = false;
    public static boolean SHOW_LEVEL_DEBUG = false;

    //A level data is the background information for storing the level
    //A loaded level is a level that is being played
    private LevelData[] levelData;

    private LoadedLevel currentLevel;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    private List<Chapter> chapters;
    private int currentChapter = 0;
    private IntMap<FileHandle> mapFileMap;

    private final TmxMapLoader.Parameters params;
    private final CustomTmxMapLoader loader;


    public LevelHandler() {
        chapters = new ArrayList<>();

        params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Linear;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;

        loader = new CustomTmxMapLoader();

        tileMapRenderer = new OrthogonalTiledMapRenderer(null, 1f, SlimeGame.getGame().getSpriteBatch());

        initialize();

        setLevel(0);
    }

    public void resetLevel() {
        currentLevel.reset();
    }

    public boolean isLevelNull(int levelIndex) {
        return !mapFileMap.containsKey(levelIndex) || mapFileMap.get(levelIndex) == null;
    }

    public void setLevel(int levelIndex) {
        //Make sure that regardless of index given, we set the level to an existing map
        levelIndex = levelIndex % levelData.length;
        if (mapFileMap.get(levelIndex) == null) {
            setLevel(levelIndex + 1);
            return;
        }

        //Reset level for when/if we ever use it again
        if (currentLevel != null) {
            currentLevel.reset();
            if (levelIndex == currentLevel.getId()) {
                return;
            }
        }

        //Load if needed
        if (levelData[levelIndex] == null) {
            loadLevel(levelIndex);
        }

        //Set some variables
        currentLevel = new LoadedLevel(levelData[levelIndex]);
        tileMapRenderer.setMap(currentLevel.getRenderMap());
        currentLevel.getRenderMap().getLayers().get("Gameplay").setVisible(SHOW_LEVEL_DEBUG);
    }

    public void advanceLevel() {
        setLevel((currentLevel.getId() + 1) % levelData.length);
    }

    public int getLevelCount() {
        return levelData.length;
    }

    public void reloadLevel(int id) {
        loadLevel(id, true);
    }

    public void loadLevel(int id) {
        loadLevel(id, false);
    }

    private void loadLevel(int id, boolean reload) {
        Chapter chapter = null;
        for (Chapter chapter1 : chapters) {
            if (chapter1.lastLevelID > id) {
                chapter = chapter1;
                break;
            }
        }

        if (levelData[id] != null && !reload) {
            return;
        }
        if (mapFileMap.get(id) == null) {
            Logger.error("Tried to load map @ chapter: " + chapter.name + " id: " + id + " but file handle wasn't registered on initialize");
            return;
        }

        FileHandle handle = mapFileMap.get(id);

        Logger.debug("Loading map @ chapter: " + chapter.name + " id: " + id + " name: " + handle.name());
        TiledMap tiledMap = loader.load("maps/chapters/" + chapter.name.toLowerCase() + "/" + handle.name(), params);

        //TODO
//        if (i == 0) {
//                    FileWriter fileWriter = new FileWriter("../assets/test.tmx");
//                    new CustomTmxMapWriter(fileWriter).tmx(tiledMap, CustomTmxMapWriter.Format.CSV);
//                    fileWriter.flush();
//                    fileWriter.close();
//        }

        try {
            levelData[id] = new LevelData(tiledMap, handle.nameWithoutExtension(), id);
        } catch (Exception e) {
            Logger.error("Could not load level data for level: " + id);
            Logger.error(e);
        }
    }

    public void initialize() {
        mapFileMap = new IntMap<>();

        FileHandle levelAtlas = Gdx.files.internal("maps/chapters/chapters.txt");


        LevelHandler.LOAD_MAPS_LOCAL = false;
        if (Gdx.files.local("maps/chapters/chapters.txt").exists()) {
            Logger.debug("Using local maps instead of internal.");
            levelAtlas = Gdx.files.local("maps/chapters/chapters.txt");
            LevelHandler.LOAD_MAPS_LOCAL = true;
        }

        BufferedReader bufferedReader;
        String line;

        int levelID = 0;
        List<String> chapterNames = new ArrayList<>();
        try {
            //Load chapters
            bufferedReader = new BufferedReader(levelAtlas.reader());
            while ((line = bufferedReader.readLine()) != null) {
                chapterNames.add(line.split(":")[1].replace(" ", ""));
            }
            bufferedReader.close();

            //Load levels in each chapter
            for (String chapterName : chapterNames) {
                int firstID = levelID;
                int lastID = 0;

                bufferedReader = new BufferedReader(getFileHandle("maps/chapters/" + chapterName.toLowerCase() + ".txt").reader());
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split(":");
                    if (split.length == 1 || split[1].isEmpty() || split[1].equals(" ")) {
                        Logger.debug("Null level: chapter: " + chapterName + " ID: " + (levelID - firstID));
                        mapFileMap.put(levelID, null);
                    } else {
                        String levelName = split[1].replace(" ", "");
                        mapFileMap.put(levelID, getFileHandle("maps/chapters/" + chapterName.toLowerCase() + "/" + levelName + ".tmx"));
                    }
                    lastID = levelID;

                    levelID++;
                }

                chapters.add(new Chapter(chapterName, firstID, lastID));

                bufferedReader.close();
            }
        } catch (IOException e) {
            Logger.error("Could not load map file list.");
            Logger.error(e);
        }
        levelData = new LevelData[mapFileMap.size];
    }

    public FileHandle getFileHandle(String path) {
        if (LevelHandler.LOAD_MAPS_LOCAL) {
            return Gdx.files.local(path);
        } else {
            return Gdx.files.internal(path);
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
        SlimeGame.getGame().getGameCamera().debug(dTime);
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
