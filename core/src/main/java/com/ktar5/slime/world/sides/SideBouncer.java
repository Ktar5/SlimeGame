package com.ktar5.slime.world.sides;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideBouncer extends TileSide {

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement, MultisidedTile tile) {
        player.setLastMovedDirection(movement);
        return false;
    }
}
