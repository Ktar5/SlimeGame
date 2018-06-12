package com.ktar5.slime.grid;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.utilities.common.constants.Direction;

public abstract class SpecialTile extends Tile {
    public final Animation<TextureRegion> animationData;
    protected Direction direction;

    protected SpecialTile(Tile tile, Direction directio, Animation<TextureRegion> animationData) {
        super(tile.x, tile.y, TileType.AIR);
        this.direction = direction;
        this.animationData = animationData;
    }

    public abstract void tick();

    protected abstract void predestroy();

    public Tile destroy() {
        //Pause any activitty that it is currently doing
        //Drop all items that it is currently holding
        predestroy();
        return new Tile(x, y, TileType.AIR);
    }

}