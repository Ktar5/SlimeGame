package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Hole extends WholeTile {
    boolean filled = false;

    public Hole(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        filled = false;
    }


    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return true;
    }

    @Override
    public void onPlayerCross(JumpPlayer player) {
        if (!filled) {
            player.kill();
        }
    }
}
