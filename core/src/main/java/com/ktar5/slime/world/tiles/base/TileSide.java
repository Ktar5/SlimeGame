package com.ktar5.slime.world.tiles.base;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;

public abstract class TileSide {
    public void tick() {
    }

    public void onPlayerHitTile(JumpPlayer player, Side hit, MultisidedTile tile) {
    }

    public void onPlayerTouchSide(JumpPlayer player, Side movement, Side touched, MultisidedTile tile) {
    }

    public void onPlayerCross(JumpPlayer player, MultisidedTile tile) {
    }

    public abstract boolean canCrossThrough(JumpPlayer player, Side movement, MultisidedTile tile);

    //public abstract Side nextBlockAfterCross(JumpPlayer player, Side movement);

    public boolean changeMovement(JumpPlayer player, Side movement, MultisidedTile tile) {
        return false;
    }
}
