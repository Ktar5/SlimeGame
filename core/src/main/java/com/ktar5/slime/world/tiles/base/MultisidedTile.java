package com.ktar5.slime.world.tiles.base;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;

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
    public void onHitTile(Entity entity, Side hit) {
        sides[hit.ordinal()].onPlayerHitTile(entity, hit, this);
    }

    @Override
    public void onTouchSide(Entity entity, Side movement, Side touched) {
        sides[touched.ordinal()].onPlayerTouchSide(entity, movement, touched, this);
    }

    @Override
    public void onCross(Entity entity) {
        //todo
    }

    @Override
    public boolean changeMovement(Entity entity, Side movement) {
        return sides[movement.opposite().ordinal()].changeMovement(entity, movement, this);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return sides[movement.opposite().ordinal()].canCrossThrough(entity, movement, this);
    }

    public TileSide getSide(Side side) {
        return sides[side.ordinal()];
    }
}
