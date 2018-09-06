package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.slime.world.Grid;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.base.Tile;
import com.ktar5.slime.world.tiles.base.TileType;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

@Getter
public class Level {
    private static boolean showDebugLevel = false;

    private final HashSet<String> lamePropertyNames = new HashSet<>(Arrays.asList(
            "width", "id", "height", "x", "y"
    ));
    protected Pair spawn;
    protected TiledMap tileMap;
    protected LevelRef levelRef;
    protected Grid grid;

    public Level(TiledMap tilemap, LevelRef levelRef) {
        this.tileMap = tilemap;
        this.levelRef = levelRef;

        initializeGrid();
    }

    public void setLevelDebug(boolean debug){
        showDebugLevel = debug;
        tileMap.getLayers().get("Gameplay").setVisible(debug);
    }

    private void initializeGrid() {
        System.out.println("Loading level: " + levelRef.path);

        LevelLoadingChecker check = new LevelLoadingChecker();

        MapLayers layers = this.getTileMap().getLayers();
        MapLayer gameplayLayer = null;
        MapLayer propertiesLayer = null;
        for (MapLayer layer : layers) {
            layer.setOffsetX(-8);
            layer.setOffsetY(8);
            if (layer.getName().startsWith("Art")) {
//                layer.setOffsetX(-8);
//                layer.setOffsetY(8);
            } else if (layer.getName().equalsIgnoreCase("Gameplay")) {
                if (check.hasGameplayLayer) check.moreThanOneGameplayLayer = true;
                gameplayLayer = layer;
                gameplayLayer.setVisible(showDebugLevel);
                check.hasGameplayLayer = true;
            } else if (layer.getName().equalsIgnoreCase("Properties")) {
                if (check.hasPropertiesLayer) check.moreThanOnePropertiesLayer = true;
                propertiesLayer = layer;
                check.hasPropertiesLayer = true;
            } else {
                check.allLayersKnown = false;
            }
        }

        if (gameplayLayer == null) {
            return;
        }

        if (!(gameplayLayer instanceof TiledMapTileLayer)) {
            return;
        }
        check.gameplayLayerInstanceofTileMapLayer = true;

        TiledMapTileLayer gpTileLayer = ((TiledMapTileLayer) gameplayLayer);

        //Load gameplay layer
        Grid newGrid = new Grid(gpTileLayer.getWidth(), gpTileLayer.getHeight());
        TiledMapTileLayer.Cell cell;
        TileType tempType;
        for (int h = 0; h < gpTileLayer.getHeight(); h++) {
            for (int w = 0; w < gpTileLayer.getWidth(); w++) {
                cell = gpTileLayer.getCell(w, h);
                if (cell == null) {
                    newGrid.grid[w][h] = new Air(w, h);
                    continue;
                }
                tempType = TileType.tileFromId(cell.getTile().getId() - 2395);
                if (tempType != null && tempType.generator != null) {
                    newGrid.grid[w][h] = tempType.generator.getTile(w, h, cell);
                } else {
                    newGrid.grid[w][h] = new Air(w, h);
                    loadSpecialCase(tempType, w, h, cell);
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
                check.hasStart = true;
            } else {
                Tile tile = grid.grid[(int) object.getRectangle().x / 16][(int) object.getRectangle().y / 16];

                //Remove useless properties
                MapProperties objectProperties = new MapProperties();
                MapProperties properties = object.getProperties();
                for (Iterator<String> iter = properties.getKeys(); iter.hasNext(); ) {
                    String name = iter.next();
                    if (lamePropertyNames.contains(name)) {
                        continue;
                    }
                    objectProperties.put(name, properties.get(name));
                }
                tile.giveProperties(objectProperties);
            }
        }

        //Load entities
        // TODO

        System.out.println("Finished loading level: " + levelRef.path);
    }

    public void loadSpecialCase(TileType tileType, int w, int h, TiledMapTileLayer.Cell cell) {

    }

    public int getSpawnX() {
        return spawn.x;
    }

    public int getSpawnY() {
        return spawn.y;
    }

}
