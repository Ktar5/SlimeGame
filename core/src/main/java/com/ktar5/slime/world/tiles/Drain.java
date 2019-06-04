package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
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
    public void onCross(Entity entity) {
        if (entity.isPlayer()) {
            JumpPlayer player = (JumpPlayer) entity;
            if (player.isSmall()) {
                player.kill("drain");
            } else {
                player.setSmall(true);
            }
        }
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }
}
