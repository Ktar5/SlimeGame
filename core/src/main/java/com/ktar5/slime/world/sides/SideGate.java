package com.ktar5.slime.world.sides;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.tiles.base.MultisidedGameTile;
import com.ktar5.slime.world.tiles.base.TileSide;

public class SideGate extends TileSide {

    //Winner gate

    @Override
    public void onPlayerHitTile(Entity entity, Side hit, MultisidedGameTile tile) {
        SlimeGame.getGame().getLevelHandler().advanceLevel();
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement, MultisidedGameTile tile) {
        return false;
    }
}
