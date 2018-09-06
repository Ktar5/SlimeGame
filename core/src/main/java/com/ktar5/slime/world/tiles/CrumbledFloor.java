package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class CrumbledFloor extends WholeTile {
    public boolean crumbled = false;

    public CrumbledFloor(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        crumbled = false;
    }

    @Override
    //todo test if this should be true
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return true;
    }

    @Override
    public void onPlayerCross(JumpPlayer player) {
        if (crumbled) {
            player.kill();
        } else {
            crumbled = true;
        }
    }
}
