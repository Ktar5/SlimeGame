package com.ktar5.slime.world.sides;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.level.LevelRef;
import com.ktar5.slime.world.tiles.base.MultisidedTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideGate extends TileSide {

    //Winner gate

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit, MultisidedTile tile) {
        int ordinal = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getLevelRef().ordinal();
        ordinal += 1;
        if (ordinal > LevelRef.values().length - 1) {
            ordinal = 0;
        }
        SlimeGame.getGame().getLevelHandler().setLevel(LevelRef.values()[ordinal]);
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement, MultisidedTile tile) {
        return false;
    }
}
