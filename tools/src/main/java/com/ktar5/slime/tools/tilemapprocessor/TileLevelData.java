package com.ktar5.slime.tools.tilemapprocessor;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
public class TileLevelData {
    private final HashSet<String> lamePropertyNames = new HashSet<>(Arrays.asList(
            "width", "id", "height", "x", "y"
    ));
    protected Pair spawn;
    protected TiledMap tileMap;
    protected int id;
    private List<EntityData> initialEntityData;
    private int gameplayArtLayerIndex;
    private int[] foregroundLayers, backgroundLayers;

    public TileLevelData(TiledMap tilemap, int id) {
        this.tileMap = tilemap;
        this.id = id;
        initialEntityData = new ArrayList<>();
        processMap();
        addGradient();
    }

    private void addGradient(){
//        TiledMapTileLayer gameplayLayer = (TiledMapTileLayer) tileMap.getLayers().get(gameplayArtLayerIndex);
//        MapLayer layer = new TiledMapTileLayer(
//                gameplayLayer.getWidth(),
//                gameplayLayer.getHeight(),
//                (int) gameplayLayer.getTileWidth(),
//                (int) gameplayLayer.getTileHeight());
//        tileMap.getLayers().add(layer);
//        for (int y = 0; y < gameplayLayer.getHeight(); y++) {
//            for (int x = 0; x < gameplayLayer.getWidth(); x++) {
//                if(tileMap.get){
//
//                }
//
//
//            }
//        }
    }

    private void processMap() {
        System.out.println("Processing level: " + id);
        LevelLoadingChecker check = new LevelLoadingChecker();

        section("Analyzing layers... ");
        MapLayers layers = this.tileMap.getLayers();
        MapLayer gameplayLayer = null;
        MapLayer propertiesLayer = null;
        List<Integer> foregrounds = new ArrayList<>();
        List<Integer> backgrounds = new ArrayList<>();
        for (MapLayer layer : layers) {

            //Check for Art layers
            if (layer.getName().startsWith("Art")) {
                //Check if the layer should appear in front of other layers
                if (layer.getProperties().containsKey("Front"))
                    foregrounds.add(layers.getIndex(layer));
                else
                    backgrounds.add(layers.getIndex(layer));

                //Check for the layer with the gameplay art tiles
                if (layer.getProperties().containsKey("Modify"))
                    gameplayArtLayerIndex = layers.getIndex(layer);
            }

            //Check for Gameplay layer
            else if (layer.getName().equalsIgnoreCase("Gameplay")) {
                if (gameplayLayer != null) err("Two layers titled 'Gameplay' exist.");

                gameplayLayer = layer;
                foregrounds.add(layers.getIndex(layer));
            }

            //Check for Properties layer
            else if (layer.getName().equalsIgnoreCase("Properties")) {
                if (propertiesLayer != null) err("Two layers titled 'Properties' exist.");

                propertiesLayer = layer;
            } else {
                err("Layer with name: '" + layer.getName() + "' has improper naming.");
            }
        }
        done();

        if (!foregrounds.isEmpty()) {
            foregroundLayers = ArrayUtils.toPrimitive(foregrounds.toArray(new Integer[0]));
        } else {
            foregroundLayers = new int[0];
        }
        backgroundLayers = ArrayUtils.toPrimitive(backgrounds.toArray(new Integer[0]));

        section("Making sure gameplay and properties layers exist");
        if (gameplayLayer == null) err("Gameplay layer is null!!");
        if (propertiesLayer == null) err("Properties layer is null!!");
        if (!(gameplayLayer instanceof TiledMapTileLayer)) {
            err("Gameplay layer is not a Tile layer!!");
            return;
        }
        done();

        TiledMapTileLayer gpTileLayer = ((TiledMapTileLayer) gameplayLayer);

        section("Test-loading Gameplay layer");
        //Load gameplay layer
        Grid grid = new Grid(gpTileLayer.getWidth(), gpTileLayer.getHeight());
        TiledMapTileLayer.Cell cell;
        TileObjectTypes tempType;
        for (int h = 0; h < gpTileLayer.getHeight(); h++) {
            for (int w = 0; w < gpTileLayer.getWidth(); w++) {
                cell = gpTileLayer.getCell(w, h);
                if (cell == null) {
                    grid.grid[w][h] = new Air(w, h);
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
                    grid.grid[w][h] = ((Tile) tempType.generator.get(w, h, cell));
                } else if (tempType.isEntity()) {
                    //Load an entity
                    grid.grid[w][h] = new Air(w, h);
                    initialEntityData.add(((EntityData) tempType.generator.get(w, h, cell)));
                } else {
                    grid.grid[w][h] = new Air(w, h);
                    System.out.println("Unknown type? Tile at " + w + ", " + h + "with ID " + (i));
                }
            }
        }
        done();

        section("Test-loading properties");
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
        done();

        System.out.println("Finished processing level: " + id);
        System.out.println();
    }

    public void err(String message) {
        throw new RuntimeException("An error has occurred during the processing of Level #" + id + ". " +
                "Here is the error message: " + message);
    }

    public void section(String sectionName) {
        System.out.print(id + ": " + sectionName + "... ");
    }

    public void done() {
        System.out.println("Done!");
    }

}
