package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
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
        if(player.isSmall()){
            player.kill();
        }else{
            player.setSmall(true);
        }
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return true;
    }
}
