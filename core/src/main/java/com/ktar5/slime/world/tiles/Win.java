package com.ktar5.slime.world.tiles;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Win extends WholeTile {
    public Win(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        SlimeGame.getGame().getLevelHandler().advanceLevel();
    }

    @Override
    public void reset() {

    }

}
