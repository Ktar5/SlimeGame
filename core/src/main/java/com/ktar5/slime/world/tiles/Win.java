package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Win extends WholeTile {
    public Win(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return false;
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {
        if (entity.isPlayer()) {
            SlimeGame.getGame().getLevelHandler().advanceLevel();
        }
    }

    @Override
    public void reset() {

    }

}
