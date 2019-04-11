package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Spikes extends WholeTile {
    public Spikes(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return false;
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {
        if (entity.isPlayer()) {
            ((JumpPlayer) entity).kill();
        }
    }

    @Override
    public void reset() {

    }

}
