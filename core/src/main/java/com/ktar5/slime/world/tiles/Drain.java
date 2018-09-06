package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Drain extends WholeTile {

    public Drain(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }

    @Override
    public void onPlayerCross(JumpPlayer player) {
        player.kill();

        super.onPlayerCross(player);
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        if (player.isSmall()) {
            //todo
        }
        return false;
    }
}
