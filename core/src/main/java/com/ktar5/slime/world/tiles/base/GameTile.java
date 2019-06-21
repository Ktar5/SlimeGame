package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.PropertyConsumer;
import com.ktar5.gameengine.util.Side;
import lombok.Getter;

@Getter
public abstract class GameTile implements PropertyConsumer {
    public final int x, y;
    private final String id;
    private Rotation rotation;

    protected GameTile(int x, int y, Rotation rotation) {
        this.id = x + "." + y;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    /**
     * @return true if should continue with movement
     */
    public boolean preMove(Entity entity) {
        return true;
    }

    public void tick() {
    }

    public abstract void reset();

    public void onHitTile(Entity entity, Side hit) {
    }

    public void onTouchSide(Entity entity, Side movement, Side touched) {
    }

    /**
     * @return true if should stop movement and snap to center of current tile
     * false if otherwise
     */
    public boolean onCross(Entity entity) {
        return false;
    }

    @Override
    public void processProperty(MapProperties properties) {

    }

    public abstract boolean canCrossThrough(Entity entity, Side movement);

//    public void setTextureOfTile(int id) {
//        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
//        currentLevel.getGameplayArtLayer().getCell(x, y).setTile(currentLevel.getTileMap().getTileSets().getTile(id));
//    }

    //public abstract Side nextBlockAfterCross(JumpPlayer player, Side movement);

    public boolean changeMovement(Entity entity, Side movement) {
        return false;
    }
}

