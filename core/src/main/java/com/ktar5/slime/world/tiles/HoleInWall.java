package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.entities.player.ShapeState;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class HoleInWall extends WholeGameTile {
    private Side sideOne;
    private Side sideTwo;
    private boolean allSides = false;

    public HoleInWall(int x, int y, Rotation rotation, Side sideOne, Side sideTwo) {
        super(x, y, rotation);
        this.sideOne = sideOne.rotateClockwise(rotation.ordinal());
        this.sideTwo = sideTwo.rotateClockwise(rotation.ordinal());
    }

    public HoleInWall(int x, int y) {
        super(x, y, Rotation.DEG_0);
        this.allSides = true;
    }

    @Override
    public void reset() {

    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            JumpPlayer player = (JumpPlayer) entity;
            if (player.getShape().equals(ShapeState.TINY)) {
                if (allSides) {
                    return true;
                } else {
                    return movement.opposite().equals(sideOne) || movement.opposite().equals(sideTwo);
                }
            }
        }
        return false;
    }

    @Override
    public boolean changeMovement(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            if (allSides || sideOne.opposite().equals(sideTwo)) {
                return false;
            }
            //Logger.debug("Values: " + sideOne.name() + ", " + sideTwo.name());
            if (movement.opposite().equals(sideOne)) {
                //Logger.debug("Entered: " + sideOne.name() + " exited: " + sideTwo.name());
                entity.setLastMovedDirection(sideTwo);
                return true;
            } else if (movement.opposite().equals(sideTwo)) {
                //Logger.debug("Entered: " + sideTwo.name()  + " exited: " + sideOne.name());
                entity.setLastMovedDirection(sideOne);
                return true;
            }
        }
        return false;
    }
}
