package com.ktar5.slime.world.sides;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideWall extends TileSide {

    @Override
    public boolean canCrossThrough(Entity entity, Side movement, MultisidedTile tile) {
        return false;
    }
}
