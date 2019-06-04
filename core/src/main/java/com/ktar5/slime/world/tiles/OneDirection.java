package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class OneDirection extends WholeGameTile {
    public final Side allowedDirection;

    public OneDirection(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        allowedDirection = Side.UP.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void reset() {
    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return movement.opposite().equals(allowedDirection);
    }


}
