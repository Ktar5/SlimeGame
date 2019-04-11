package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.arrow.Arrow;
import com.ktar5.slime.entities.arrow.ArrowEntityData;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class Shooter extends WholeTile {
    public final Side shootSide;
    public int canShoot = 1;
    ArrowEntityData data;
    private static final int ticks = 60;
    int currentTicks = 0;

    public Shooter(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        shootSide = Side.DOWN.rotateClockwise(rotation.ordinal());
        data = new ArrowEntityData(this.x + shootSide.x, this.y + shootSide.y, shootSide);
    }

    @Override
    public void reset() {
        canShoot = 1;
    }

    @Override
    public void tick() {
        currentTicks += 1;
        if (currentTicks >= ticks) {
            currentTicks = 0;
            shoot();
        }
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {

    }

    @Override
    public void onCross(Entity entity) {

    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return false;
    }


    public void shoot() {
        new Arrow(data);
    }

}