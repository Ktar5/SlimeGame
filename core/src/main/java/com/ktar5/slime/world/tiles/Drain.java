package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.entities.player.ShapeState;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Drain extends WholeGameTile {

    public Drain(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean onCross(Entity entity) {
        if (entity.isPlayer()) {
            JumpPlayer player = (JumpPlayer) entity;
            if (player.getShape().equals(ShapeState.TINY)) {
                player.kill("drain");
                return true;
            } else if (!player.getShape().equals(ShapeState.METAL)) {
                player.setShape(ShapeState.TINY);
            }
        }
        return false;
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }
}
