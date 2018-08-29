package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.tiles.WholeTile;

public class Drain extends WholeTile {
    
    public Drain(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void onPlayerCross(JumpPlayer player) {
        player.killPlayer();
        
        super.onPlayerCross(player);
    }
    
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        if(player.isSmall() ){
        }
        return false;
    }
}
