package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Spikes extends WholeTile {
    public Spikes(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        player.kill();
    }

    @Override
    public void reset() {

    }

}
