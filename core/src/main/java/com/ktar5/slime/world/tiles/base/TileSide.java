package com.ktar5.slime.world.tiles.base;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;

public abstract class TileSide {
    public void tick() {
    }

    public void onPlayerHitTile(Entity entity, Side hit, MultisidedTile tile) {
    }

    public void onPlayerTouchSide(Entity entity, Side movement, Side touched, MultisidedTile tile) {
    }

    public void onPlayerCross(Entity entity, MultisidedTile tile) {
    }

    public abstract boolean canCrossThrough(Entity entity, Side movement, MultisidedTile tile);

    //public abstract Side nextBlockAfterCross(Entity entity, Side movement);

    public boolean changeMovement(Entity entity, Side movement, MultisidedTile tile) {
        return false;
    }
}
