package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class OneDirection extends WholeTile {
    public final Side allowedDirection;

    public OneDirection(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        allowedDirection = Side.UP.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void reset() {
    }


    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return movement.opposite().equals(allowedDirection);
    }


}
