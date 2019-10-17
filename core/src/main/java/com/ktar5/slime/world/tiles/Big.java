package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.entities.player.ShapeState;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Big extends WholeGameTile {

    public Big(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean onCross(Entity entity) {
        if (entity.isPlayer()) {
            JumpPlayer player = (JumpPlayer) entity;
            player.setShape(ShapeState.NORMAL);
        }
        return false;
    }

    @Override
    public boolean canCrossThrough(Entity player, Side movement) {
        return true;
    }
}
