package com.ktar5.slime.world;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.slime.grid.Grid;
import com.ktar5.slime.grid.Tile;
import com.ktar5.slime.grid.TileType;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;

@Getter
public class Level {
    private Pair spawn;
    @Deprecated
    private TiledMap tileMap;
    private Levels levelRef;
    
    private Grid grid;
    
    public Level(Pair spawn, TiledMap tileMap, Levels levelRef) {
        this.spawn = spawn;
        this.tileMap = tileMap;
        this.levelRef = levelRef;
        
        initializeGrid();
    }
    
    private void initializeGrid() {
        final TiledMapTileLayer layer = (TiledMapTileLayer) this.getTileMap().getLayers().get(0);
        Grid newGrid = new Grid(layer.getWidth(), layer.getHeight());
        TiledMapTileLayer.Cell cell;
        for (int h = 0; h < layer.getHeight(); h++) {
            for (int w = 0; w < layer.getWidth(); w++) {
                if (!newGrid.isInMapRange(w, h))
                    continue;
                cell = layer.getCell(w, h);
                if (cell == null) {
                    newGrid.grid[w][h] = new Tile(w, h, TileType.AIR);
                    continue;
                }
                newGrid.grid[w][h] = new Tile(w, h, TileType.tileFromId(cell.getTile().getId() - 1));
            }
        }
        grid = newGrid;
    }
    
    public int getSpawnX() {
        return spawn.x;
    }
    
    public int getSpawnY() {
        return spawn.y;
    }
    
}
