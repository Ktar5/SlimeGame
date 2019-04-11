package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Bouncer extends WholeTile {
    public Bouncer(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        entity.setLastMovedDirection(movement);
        return false;
    }
}
