package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Gate extends WholeTile {
    public final Side opening;
    public boolean open = false;

    public Gate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        opening = Side.DOWN.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void reset() {
        open = false;
    }


    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return open && (movement.opposite().equals(opening) || movement.equals(opening));
    }
}
