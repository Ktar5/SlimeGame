package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.TileType;
import com.ktar5.slime.world.grid.tiles.WholeTile;

public class Wall extends WholeTile {
    public Wall(int x, int y) {
        super(x, y, TileType.WALL);
    }
    
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }
}
