package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableTile;

public class PressurePlate extends TriggerableTile {
    public boolean pressed = false;

    public PressurePlate(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void reset() {
        pressed = false;
    }


    @Override
    public void onCross(Entity entity) {
        if (!pressed) {
            pressed = true;
            callEvent(Trigger.ON_PASS);
        }
    }

}
