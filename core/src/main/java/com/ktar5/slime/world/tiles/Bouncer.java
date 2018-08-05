package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.TileType;
import com.ktar5.slime.world.grid.tiles.WholeTile;

public class Bouncer extends WholeTile {
    public Bouncer(int x, int y) {
        super(x, y, TileType.MISC);
    }
    
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        player.setLastMovedDirection(movement);
        return false;
    }
}