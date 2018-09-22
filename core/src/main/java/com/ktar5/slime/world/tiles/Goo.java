package com.ktar5.slime.world.tiles;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.player.states.Idle;
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
        player.getEntityState().changeStateAfterUpdate(Idle.class);
        System.out.println(player.position.x);
        System.out.println(player.position.y);
        int x = (int) player.position.x;
        int y = (int) player.position.y;
        player.position.set(x, y);
        //player.position.set(x + .5f, y);
        System.out.println(".....");
        System.out.println(player.position.x);
        System.out.println(player.position.y);
        //player.position.set();
    }
}
