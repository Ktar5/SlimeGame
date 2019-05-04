package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.world.Grid;
import com.ktar5.slime.world.level.types.TileObjectTypes;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.base.Tile;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

@Getter
public class Level {
    private static boolean showDebugLevel = false;

    private final HashSet<String> lamePropertyNames = new HashSet<>(Arrays.asList(
            "width", "id", "height", "x", "y"
    ));
    protected Pair spawn;
    protected TiledMap tileMap;
    protected int id;
    protected Grid grid;
    protected boolean[][] slimeCovered;
    private List<EntityData> initialEntityData;
    private int gameplayArtLayerIndex;
    private int[] foregroundLayers, backgroundLayers;

    public Level(TiledMap tilemap, int id) {
        this.tileMap = tilemap;
        this.id = id;
        initialEntityData = new ArrayList<>();
        initializeGrid();
        slimeCovered = new boolean[this.grid.width][this.grid.height];
    }

    public void setLevelDebug(boolean debug) {
        showDebugLevel = debug;
        tileMap.getLayers().get("Gameplay").setVisible(debug);
    }

    public void toggleDebug() {
        setLevelDebug(!showDebugLevel);
    }

    public TiledMapTileLayer getGameplayArtLayer() {
        return ((TiledMapTileLayer) tileMap.getLayers().get(gameplayArtLayerIndex));
    }

    private void initializeGrid() {
        System.out.println("Loading level: " + id);

        MapLayers layers = this.getTileMap().getLayers();
        MapLayer gameplayLayer = null;
        MapLayer propertiesLayer = null;
        List<Integer> foregrounds = new ArrayList<>();
        List<Integer> backgrounds = new ArrayList<>();
        for (MapLayer layer : layers) {
            if (layer.getName().equals("Art_Image")) {
                TiledMapImageLayer layer1 = (TiledMapImageLayer) layer;
                layer1.setX(-8);
                layer1.setY(-8);
            } else {
                layer.setOffsetX(-8);
                layer.setOffsetY(8);
            }
            if (layer.getName().startsWith("Art")) {
                if (layer.getProperties().containsKey("Front")) {
                    foregrounds.add(layers.getIndex(layer));
                } else {
                    backgrounds.add(layers.getIndex(layer));
                }
                if (layer.getName().equals("Art_Gameplay")) {
                    gameplayArtLayerIndex = layers.getIndex(layer);
                }
            } else if (layer.getName().equalsIgnoreCase("SlimeCover")) {
                backgrounds.add(layers.getIndex(layer));
            } else if (layer.getName().equalsIgnoreCase("Gameplay")) {
                gameplayLayer = layer;
                layer.setVisible(false);
                layer.setOpacity(.3f);
                foregrounds.add(layers.getIndex(layer));
                gameplayLayer.setVisible(showDebugLevel);
            } else if (layer.getName().equalsIgnoreCase("Properties")) {
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
        Grid newGrid = new Grid(gpTileLayer.getWidth(), gpTileLayer.getHeight());
        TiledMapTileLayer.Cell cell;
        TileObjectTypes tempType;
        for (int h = 0; h < gpTileLayer.getHeight(); h++) {
            for (int w = 0; w < gpTileLayer.getWidth(); w++) {
                cell = gpTileLayer.getCell(w, h);
                if (cell == null) {
                    newGrid.grid[w][h] = new Air(w, h);
                    continue;
                }
                //TODO magic value (2395)
                int i = cell.getTile().getId() - 2395;
                if (i < 0) {
                    i = cell.getTile().getId() - 1;
                }
                tempType = TileObjectTypes.tileFromId(i);
                if (tempType == null || tempType.generator == null) {
                    System.out.println("Don't know what to do with tile at " + w + ", " + h + "with ID " + (i));
                } else if (tempType.isTile()) {
                    //Load a tile
                    newGrid.grid[w][h] = ((Tile) tempType.generator.get(w, h, cell));
                } else if (tempType.isEntity()) {
                    //Load an entity
                    newGrid.grid[w][h] = new Air(w, h);
                    initialEntityData.add(((EntityData) tempType.generator.get(w, h, cell)));
                } else {
                    newGrid.grid[w][h] = new Air(w, h);
                    System.out.println("Unknown type? Tile at " + w + ", " + h + "with ID " + (i));
                }
            }
        }
        grid = newGrid;

        //Load properties
        for (MapObject o : propertiesLayer.getObjects()) {
            if (!(o instanceof RectangleMapObject)) {
                System.out.println("Found a non rectangle object");
                continue;
            }
            RectangleMapObject object = ((RectangleMapObject) o);
            if (object.getName() != null && object.getName().equalsIgnoreCase("spawn")) {
                this.spawn = new Pair((int) object.getRectangle().x / 16, (int) object.getRectangle().y / 16);
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

                Tile tile = grid.grid[(int) object.getRectangle().x / 16][(int) object.getRectangle().y / 16];
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

        System.out.println("Finished loading level: " + id);
    }

    public int getSpawnX() {
        return spawn.x * 16;
    }

    public int getSpawnY() {
        return spawn.y * 16;
    }

}
