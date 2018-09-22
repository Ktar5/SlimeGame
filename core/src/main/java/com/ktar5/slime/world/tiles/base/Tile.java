package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.player.JumpPlayer;
import lombok.Getter;

@Getter
public abstract class Tile {
    public final int x, y;
    private final String id;
    private Rotation rotation;

    protected Tile(int x, int y, Rotation rotation) {
        this.id = x + "." + y;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    /**
     * @return false if should NOT continue with movement
     */
    public boolean preMove(JumpPlayer player){
        return true;
    }

    public void tick() {
    }

    public abstract void reset();

    public void onPlayerHitTile(JumpPlayer player, Side hit) {
    }

    public void onPlayerTouchSide(JumpPlayer player, Side movement, Side touched) {
    }

    public void onPlayerCross(JumpPlayer player) {
    }

    public abstract boolean canCrossThrough(JumpPlayer player, Side movement);

    public void giveProperties(MapProperties properties) {
    }

    //public abstract Side nextBlockAfterCross(JumpPlayer player, Side movement);

    public boolean changeMovement(JumpPlayer player, Side movement) {
        return false;
    }
}

