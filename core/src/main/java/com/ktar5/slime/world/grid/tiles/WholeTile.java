package com.ktar5.slime.world.grid.tiles;

import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.TileType;

public abstract class WholeTile extends Tile {
    private TileType tileType;
    
    public WholeTile(int x, int y, TileType tileType) {
        super(x, y);
        this.tileType = tileType;
    }
    
    @Override
    public TileType getType(Side side) {
        return tileType;
    }
}
