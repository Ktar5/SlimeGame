package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableTile;

public class Button extends TriggerableTile {
    public final Side pressableSide;
    public boolean pressed = false;

    public Button(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        pressableSide = Side.DOWN.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void reset() {
        pressed = false;
    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return false;
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {
        if (hit == pressableSide) {
            if (pressed) {
                return;
            }
            pressed = true;
            callEvent(Trigger.ON_HIT);
        }
    }

}
