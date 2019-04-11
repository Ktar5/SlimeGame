package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.box.BoxIdle;
import com.ktar5.slime.entities.player.states.Idle;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Goo extends WholeTile {
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
    public void onCross(Entity entity) {
        if (entity.isPlayer()) {
            entity.getEntityState().changeStateAfterUpdate(Idle.class);
        } else if (entity instanceof Box) {
            entity.getEntityState().changeStateAfterUpdate(BoxIdle.class);
        } else {
            return;
        }
        int x = (int) entity.position.x / 16;
        int y = (int) entity.position.y / 16;
        entity.position.set(x * 16, y * 16);
    }
}
