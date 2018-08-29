package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.tiles.WholeTile;

public class HoleInWall extends WholeTile{
    private Side in;
    private Side out;
    
    public HoleInWall(int x, int y, Side in, Side out) {
        super(x, y);
        this.in = in;
        this.out = out;
    }
    
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return player.isSmall() && movement.opposite().equals(in);
    }
    
    @Override
    public boolean changeMovement(JumpPlayer player, Side movement) {
        player.setLastMovedDirection(out);
        return true;
    }
}
