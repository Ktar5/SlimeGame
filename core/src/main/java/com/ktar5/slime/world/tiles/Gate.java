package com.ktar5.slime.world.tiles;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.Levels;
import com.ktar5.slime.world.grid.TileType;
import com.ktar5.slime.world.grid.tiles.WholeTile;

public class Gate extends WholeTile {
    public Gate(int x, int y) {
        super(x, y, TileType.WIN);
    }
    
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }
    
    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        int ordinal = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getLevelRef().ordinal();
        ordinal += 1;
        if (ordinal > Levels.values().length - 1) {
            ordinal = 0;
        }
        SlimeGame.getGame().getLevelHandler().setLevel(Levels.values()[ordinal]);
    }
}
