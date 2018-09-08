package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
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
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        if (hit == pressableSide) {
            if (pressed) {
                return;
            }
            pressed = true;
            callEvent(Trigger.ON_HIT);
        }
    }

}
