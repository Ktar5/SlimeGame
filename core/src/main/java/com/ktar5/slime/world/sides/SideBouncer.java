package com.ktar5.slime.world.sides;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideBouncer extends TileSide {

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement, MultisidedTile tile) {
        player.setLastMovedDirection(movement);
        return false;
    }
}
