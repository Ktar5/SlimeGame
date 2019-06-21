package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.base.GameTile;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.tinylog.Logger;

import java.util.*;

@Getter
public class LevelData {
    private static final HashSet<String> lamePropertyNames = new HashSet<>(Arrays.asList(
            "width", "id", "height", "x", "y"
    ));

    private int width, height, id;
    private final String name;
    private final UUID uuid;
    private List<EntityData> initialEntityData;

    private GameTile[][] gameMap;
    private TiledMap renderMap;

    private Pair spawnTile;
    private int gameplayArtLayerIndex;
    private int[] foregroundLayers, backgroundLayers;

//    private boolean[][] slimeCovered;
    private int collectablesFound = 0;

    public LevelData(TiledMap renderMap, String name, int id) {
//        this.uuid = UUID.fromString(renderMap.getProperties().get("uuid", String.class));
        this.uuid = UUID.randomUUID();
        //TODO FIX UUID

        this.renderMap = renderMap;
        this.id = id;
        this.name = name;
        initialEntityData = new ArrayList<>();
        initialize();
    }

    public LevelData(LevelData levelData) {
        this.width = levelData.width;
        this.uuid = levelData.uuid;
        this.name = levelData.name;
        this.height = levelData.height;
        this.spawnTile = levelData.spawnTile;
        this.id = levelData.id;
        this.initialEntityData = levelData.initialEntityData;
        this.gameplayArtLayerIndex = levelData.gameplayArtLayerIndex;
        this.foregroundLayers = levelData.foregroundLayers;
        this.backgroundLayers = levelData.backgroundLayers;

        //Or we can initialize??
        this.gameMap = levelData.gameMap;
        this.renderMap = levelData.renderMap;
    }

    public TiledMapTileLayer getGameplayArtLayer() {
        return ((TiledMapTileLayer) renderMap.getLayers().get(gameplayArtLayerIndex));
    }

    private void entityTouchedTileSide(int x, int y, Entity entity, Side side) {
        GameTile gameTile = tileFromDirection(x, y, side);
        if (gameTile != null) {
            gameTile.onTouchSide(entity, null, side.opposite());
        }
    }

    public GameTile tileFromDirection(int x, int y, Side side) {
        x += side.x;
        y += side.y;
        if (isInMapRange(x, y)) {
            return gameMap[x][y];
        }
        return null;
    }

    public Pair pairFromDirection(int x, int y, Side side) {
        x += side.x;
        y += side.y;
        if (isInMapRange(x, y)) {
            return new Pair(x, y);
        }
        return null;
    }

    public boolean activateAllTiles(Entity entity) {
        int x = (int) entity.position.x / 16;
        int y = (int) entity.position.y / 16;
        entityTouchedTileSide(x, y, entity, Side.UP);
        entityTouchedTileSide(x, y, entity, Side.DOWN);
        entityTouchedTileSide(x, y, entity, Side.LEFT);
        entityTouchedTileSide(x, y, entity, Side.RIGHT);
        if (isInMapRange(x, y)) {
//            Logger.debug(gameMap[x][y]);
//            Logger.debug(SlimeGame.getGame().getLevelHandler().getCurrentLevel().getId());
            return gameMap[x][y].onCross(entity);
        }
        return false;
    }

    public GameTile[] getSurrounding(int x, int y) {
        List<GameTile> gameTiles = new ArrayList<>();
        if (isInMapRange(x, y + 1)) {
            gameTiles.add(this.gameMap[x][y + 1]);
        }
        if (isInMapRange(x, y - 1)) {
            gameTiles.add(this.gameMap[x][y - 1]);
        }
        if (isInMapRange(x + 1, y)) {
            gameTiles.add(this.gameMap[x + 1][y]);
        }
        if (isInMapRange(x - 1, y)) {
            gameTiles.add(this.gameMap[x - 1][y]);
        }
        return gameTiles.toArray(new GameTile[0]);
    }

    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void updateTiles(float dTime) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gameMap[x][y] != null) {
                    gameMap[x][y].tick();
                }
            }
        }
    }

    public UUID getUUID(){
        return uuid;
    }

    //TODO rewrite
    private void initialize() {
        Logger.debug("Loading level: '" + getName() + "' with id:" + id + " with uuid: " + getUUID());

        MapLayers layers = this.getRenderMap().getLayers();
        MapLayer gameplayLayer = null;
        MapLayer propertiesLayer = null;
        List<Integer> foregrounds = new ArrayList<>();
        List<Integer> backgrounds = new ArrayList<>();
        for (MapLayer layer : layers) {
            if (layer.getName().startsWith("Art") || layer.getName().startsWith("art")) {
                if (layer.getProperties().containsKey("Front") || layer.getProperties().containsKey("front")) {
                    foregrounds.add(layers.getIndex(layer));
                } else {
                    backgrounds.add(layers.getIndex(layer));
                }
                if (layer.getName().equals("Art_Gameplay")) {
                    gameplayArtLayerIndex = layers.getIndex(layer);
                }
            } else if (layer.getName().equalsIgnoreCase("SlimeCover")) {
                backgrounds.add(layers.getIndex(layer));
            } else if (layer.getName().equals("Gameplay")) {
                gameplayLayer = layer;
                layer.setVisible(false);
                layer.setOpacity(.3f);
                foregrounds.add(layers.getIndex(layer));
                gameplayLayer.setVisible(false);
            } else if (layer.getName().equals("Properties")) {
                propertiesLayer = layer;
            }
        }

        if (!foregrounds.isEmpty()) {
            foregroundLayers = ArrayUtils.toPrimitive(foregrounds.toArray(new Integer[0]));
        } else {
            foregroundLayers = new int[0];
        }
        backgroundLayers = ArrayUtils.toPrimitive(backgrounds.toArray(new Integer[0]));

        if (gameplayLayer == null) {
            return;
        }

        if (!(gameplayLayer instanceof TiledMapTileLayer)) {
            return;
        }

        TiledMapTileLayer gpTileLayer = ((TiledMapTileLayer) gameplayLayer);

        //Load gameplay layer
        width= gpTileLayer.getWidth();
        height = gpTileLayer.getHeight();
        gameMap = new GameTile[gpTileLayer.getWidth()][gpTileLayer.getHeight()];
        TiledMapTileLayer.Cell cell;
        TileObjectTypes tempType;
        for (int h = 0; h < gpTileLayer.getHeight(); h++) {
            for (int w = 0; w < gpTileLayer.getWidth(); w++) {
                cell = gpTileLayer.getCell(w, h);
                if (cell == null) {
                    gameMap[w][h] = new Air(w, h);
                    continue;
                }
                //TODO magic value (2395)
                int i = cell.getTile().getId() - 2395;
                if (i < 0) {
                    i = cell.getTile().getId() - 1;
                }
                tempType = TileObjectTypes.tileFromId(i);
                if (tempType == null || tempType.generator == null) {
                    Logger.error("Don't know what to do with tile at " + w + ", " + h + "with ID " + (i));
                } else if (tempType.isTile()) {
                    //Load a tile
                    gameMap[w][h] = ((GameTile) tempType.generator.get(w, h, cell));
                } else if (tempType.isEntity()) {
                    //Load an entity
                    gameMap[w][h] = new Air(w, h);
                    initialEntityData.add(((EntityData) tempType.generator.get(w, h, cell)));
                } else {
                    gameMap[w][h] = new Air(w, h);
                    Logger.error("Unknown type? GameTile at " + w + ", " + h + "with ID " + (i));
                }
            }
        }
        //Load properties
        for (MapObject o : propertiesLayer.getObjects()) {
            if (!(o instanceof RectangleMapObject)) {
                Logger.debug("Found a non rectangle object");
                continue;
            }
            RectangleMapObject object = ((RectangleMapObject) o);
            if (object.getName() != null && object.getName().equalsIgnoreCase("spawn")) {
                this.spawnTile = new Pair((int) object.getRectangle().x / 16, (int) object.getRectangle().y / 16);
            } else {
                //Remove useless properties
                MapProperties preProcessProperties = object.getProperties();
                MapProperties postProcessProperties = new MapProperties();
                for (Iterator<String> iter = preProcessProperties.getKeys(); iter.hasNext(); ) {
                    String name = iter.next();
                    if (lamePropertyNames.contains(name)) {
                        continue;
                    }
                    postProcessProperties.put(name, preProcessProperties.get(name));
                }

                GameTile tile = gameMap[(int) object.getRectangle().x / 16][(int) object.getRectangle().y / 16];
                if (tile instanceof Air) {
                    for (EntityData entityData : this.initialEntityData) {
                        if (entityData.initialPosition.equals(tile.x, tile.y)) {
                            entityData.processProperty(postProcessProperties);
                        }
                    }
                } else {
                    tile.processProperty(postProcessProperties);
                }
            }
        }

        Logger.debug("Finished loading level: " + id);
    }

}

