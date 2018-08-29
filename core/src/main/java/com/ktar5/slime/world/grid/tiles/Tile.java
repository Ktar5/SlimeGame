package com.ktar5.slime.world.grid.tiles;

import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.Side;
import lombok.Getter;

@Getter
public abstract class Tile {
    public final int x, y;
    private final String id;
    
    protected Tile(int x, int y) {
        this.id = x + "." + y;
        this.x = x;
        this.y = y;
    }
    
    public void tick() {
    }
    
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
    }
    
    public void onPlayerTouchSide(JumpPlayer player, Side movement, Side touched) {
    }
    
    public void onPlayerCross(JumpPlayer player) {
    }
    
    public abstract boolean canCrossThrough(JumpPlayer player, Side movement);
    
    //public abstract Side nextBlockAfterCross(JumpPlayer player, Side movement);
    
    public boolean changeMovement(JumpPlayer player, Side movement) {
        return false;
    }
}

