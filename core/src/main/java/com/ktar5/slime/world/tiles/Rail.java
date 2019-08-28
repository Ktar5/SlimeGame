package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.cart.Cart;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;
import lombok.Getter;

@Getter
public class Rail extends WholeGameTile {
    private Side sideOne;
    private Side sideTwo;
    private boolean allSides = false;
    private boolean isEnd = false;

    public Rail(int x, int y, Rotation rotation, Side sideOne, Side sideTwo) {
        super(x, y, rotation);
        this.sideOne = sideOne.rotateClockwise(rotation.ordinal());
        this.sideTwo = sideTwo.rotateClockwise(rotation.ordinal());
    }

    public Rail(int x, int y) {
        super(x, y, Rotation.DEG_0);
        this.allSides = true;
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        if (!(entity instanceof Cart)) {
            return true;
        }
        if (allSides) {
            return true;
        } else {
            return movement.opposite().equals(sideOne) || movement.opposite().equals(sideTwo);
        }
    }

    @Override
    public boolean changeMovement(Entity entity, Side movement) {
        System.out.println("444 444 444");
        if (!(entity instanceof Cart)) {
            System.out.println(5);
            return false;
        }
        if (allSides || sideOne.opposite().equals(sideTwo)) {
            return false;
        }
        //Logger.debug("Values: " + sideOne.name() + ", " + sideTwo.name());
        if (movement.opposite().equals(sideOne)) {
            //Logger.debug("Entered: " + sideOne.name() + " exited: " + sideTwo.name());
            System.out.println(1);
            entity.setLastMovedDirection(sideTwo);
            return true;
        } else if (movement.opposite().equals(sideTwo)) {
            //Logger.debug("Entered: " + sideTwo.name()  + " exited: " + sideOne.name());
            entity.setLastMovedDirection(sideOne);
            System.out.println(2);
            return true;
        }
        return false;
    }
}
