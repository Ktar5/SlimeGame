package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.box.BoxIdle;
import com.ktar5.slime.entities.player.states.Idle;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Goo extends WholeGameTile {
    public Goo(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public boolean onCross(Entity entity) {
        if (entity.isPlayer()) {
            entity.getEntityState().changeStateAfterUpdate(Idle.class);
        } else if (entity instanceof Box) {
            entity.getEntityState().changeStateAfterUpdate(BoxIdle.class);
        } else {
            return true;
        }
        int x = (int) entity.position.x / 16;
        int y = (int) entity.position.y / 16;
//        entity.position.set((x * 16) + 8, (y * 16) + 8);
        return true;
    }
}
