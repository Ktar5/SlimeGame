package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class HoleInWall extends WholeTile {
    private Side in;
    private Side out;

    public HoleInWall(int x, int y, Rotation rotation, Side in, Side out) {
        super(x, y, rotation);
        this.in = in;
        this.out = out;
    }

    @Override
    public void reset() {

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
