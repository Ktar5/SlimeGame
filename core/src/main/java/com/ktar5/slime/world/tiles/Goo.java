package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.player.states.Idle;
import com.ktar5.slime.utils.Side;
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
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return true;
    }

    @Override
    public void onPlayerCross(JumpPlayer player) {
        System.out.println("Gooped");
        System.out.println("Gooped");
        System.out.println("Gooped");
        System.out.println("Gooped");

        player.playerState.changeStateAfterUpdate(Idle.class);
    }
}
