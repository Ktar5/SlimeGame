package com.ktar5.slime.world.sides;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideSpikes extends TileSide {

    @Override
    public void onPlayerHitTile(Entity entity, Side hit, MultisidedTile tile) {


    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement, MultisidedTile tile) {
        return false;
    }
}
