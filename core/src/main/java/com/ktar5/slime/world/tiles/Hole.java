package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.player.JumpPlayer;
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
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void onCross(Entity entity) {
        if (filled) {
            return;
        }

        if(entity.isPlayer()){
            ((JumpPlayer) entity).kill();
        }else if(entity instanceof Box){
            filled = true;
            //todo remove entity
        }


    }
}
