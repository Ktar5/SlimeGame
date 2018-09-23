package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class HoleInWall extends WholeTile {
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
        if(entity.isPlayer()){
            JumpPlayer player = (JumpPlayer) entity;
            if(player.isSmall()){
                if(allSides){
                    return true;
                }else{
                    return movement.opposite().equals(sideOne) || movement.opposite().equals(sideTwo);
                }
            }
        }
        return false;
    }

    @Override
    public boolean changeMovement(Entity entity, Side movement) {
        if(entity.isPlayer()){
            if(allSides){
                return false;
            }
            System.out.println("Values: " + sideOne.name() + ", " + sideTwo.name());
            if(movement.opposite().equals(sideOne)){
                System.out.println("Entered: " + sideOne.name() + " exited: " + sideTwo.name());
                entity.setLastMovedDirection(sideTwo);
                return true;
            }else if(movement.opposite().equals(sideTwo)){
                System.out.println("Entered: " + sideTwo.name()  + " exited: " + sideOne.name());
                entity.setLastMovedDirection(sideOne);
                return true;
            }
        }
        return false;
    }
}
