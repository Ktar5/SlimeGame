package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.screens.gamestate.Winning;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Win extends WholeGameTile {
    public Win(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean onCross(Entity entity) {
        if (entity.isPlayer()) {
            ((GameScreen) SlimeGame.getGame().getScreen()).getGameState().changeStateAfterUpdate(Winning.class);
        }
        return true;
    }
}
