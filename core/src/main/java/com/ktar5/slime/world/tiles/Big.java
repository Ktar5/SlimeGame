package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Big extends WholeTile {

    public Big(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }

    @Override
    public void onCross(Entity entity) {
        if(entity.isPlayer()){
            JumpPlayer player = (JumpPlayer) entity;
            if(player.isSmall()){
                player.setSmall(false);
            }

        }
    }

    @Override
    public boolean canCrossThrough(Entity player, Side movement) {
        return true;
    }
}
