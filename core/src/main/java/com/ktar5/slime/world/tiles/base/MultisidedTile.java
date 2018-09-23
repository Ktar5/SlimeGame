package com.ktar5.slime.world.tiles.base;

import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;

public abstract class MultisidedTile extends Tile {
    private TileSide[] sides;

    public MultisidedTile(int x, int y, TileSide up, TileSide right, TileSide down, TileSide left) {
        super(x, y, Rotation.DEG_0);
        sides = new TileSide[]{up, right, down, left};
    }


    @Override
    public void tick() {
        //todo
        super.tick();
    }

    @Override
    public void onPlayerHitTile(JumpPlayer player, Side hit) {
        sides[hit.ordinal()].onPlayerHitTile(player, hit, this);
    }

    @Override
    public void onTouchSide(JumpPlayer player, Side movement, Side touched) {
        sides[touched.ordinal()].onPlayerTouchSide(player, movement, touched, this);
    }

    @Override
    public void onCross(JumpPlayer player) {
        //todo
    }

    @Override
    public boolean changeMovement(JumpPlayer player, Side movement) {
        return sides[movement.opposite().ordinal()].changeMovement(player, movement, this);
    }

    @Override
    public boolean canCrossThrough(JumpPlayer player, Side movement) {
        return sides[movement.opposite().ordinal()].canCrossThrough(player, movement, this);
    }

    public TileSide getSide(Side side) {
        return sides[side.ordinal()];
    }
}
