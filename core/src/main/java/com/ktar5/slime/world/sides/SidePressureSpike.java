package com.ktar5.slime.world.sides;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SidePressureSpike extends TileSide {
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement, MultisidedTile tile) {
        return false;
    }
}
