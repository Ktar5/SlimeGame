package com.ktar5.slime.world.sides;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideSpikes extends TileSide {

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit, MultisidedTile tile) {
        player.kill();
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement, MultisidedTile tile) {
        return false;
    }
}
