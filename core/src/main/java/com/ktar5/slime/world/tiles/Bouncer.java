package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Bouncer extends WholeGameTile {
    private Side sideOne = Side.LEFT;
    private Side sideTwo = Side.DOWN;

    public Bouncer(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        sideOne = sideOne.rotateClockwise(rotation.ordinal());
        sideTwo = sideTwo.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void reset() {

    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        if(movement.opposite().equals(sideOne) || movement.opposite().equals(sideTwo)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean changeMovement(Entity entity, Side movement) {
        if(movement.opposite().equals(sideOne)){
            entity.setLastMovedDirection(sideTwo);
        } else if(movement.opposite().equals(sideTwo)){
            entity.setLastMovedDirection(sideOne);
        }else{
            return false;
        }
        return true;
    }
}
