package com.ktar5.slime.world.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.player.states.Respawn;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.TileType;
import com.ktar5.slime.world.grid.tiles.WholeTile;

public class Spikes extends WholeTile {
    public Spikes(int x, int y) {
        super(x, y, TileType.HARMFUL);
    }
    
    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return false;
    }
    
    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        player.playerState.changeStateAfterUpdate(Respawn.class);
    }
}
