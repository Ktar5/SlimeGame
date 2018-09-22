package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableTile;

public class PressurePlate extends TriggerableTile {
    public boolean pressed = false;

    public PressurePlate(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return true;
    }

    @Override
    public void reset() {
        pressed = false;
    }


    @Override
    public void onPlayerCross(JumpPlayer player) {
        if (!pressed) {
            pressed = true;
            callEvent(Trigger.ON_PASS);
        }
    }

}
