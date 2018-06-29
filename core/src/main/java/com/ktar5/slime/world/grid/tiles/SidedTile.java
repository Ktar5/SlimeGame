package com.ktar5.slime.world.grid.tiles;

import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.TileType;

public abstract class SidedTile extends Tile {
    private TileType[] types;
    
    public SidedTile(int x, int y, TileType up, TileType right, TileType down, TileType left) {
        super(x, y);
        types = new TileType[]{up, right, down, left};
    }
    
    @Override
    public TileType getType(Side side) {
        return types[side.ordinal()];
    }
}
