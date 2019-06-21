package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Drain extends WholeGameTile {

    public Drain(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean onCross(Entity entity) {
        System.out.println("Drain cross");
        if (entity.isPlayer()) {
            System.out.println("Is player");
            JumpPlayer player = (JumpPlayer) entity;
            if (player.isSmall()) {
                System.out.println("Is small");
                player.kill("drain");
                return true;
            } else {
                System.out.println("Not small");
                player.setSmall(true);
            }
        }else{
            System.out.println("Not player");

        }
        return false;
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }
}
