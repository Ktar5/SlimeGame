package com.ktar5.slime.world.sides;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideBouncer extends TileSide {

    @Override
    public boolean canCrossThrough(Entity entity, Side movement, MultisidedTile tile) {
        entity.setLastMovedDirection(movement);
        return false;
    }
}
