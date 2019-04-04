package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.engine.util.TiledPropertyConsumer;
import com.ktar5.slime.world.level.LoadedLevel;
import lombok.Getter;

@Getter
public abstract class Tile implements TiledPropertyConsumer {
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

    public void onCross(Entity entity) {
    }

    @Override
    public void processProperty(MapProperties properties) {
    }

    public abstract boolean canCrossThrough(Entity entity, Side movement);

    public void setTextureOfTile(int id) {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.getGameplayArtLayer().getCell(x, y).setTile(currentLevel.getTileMap().getTileSets().getTile(id));
    }

    //public abstract Side nextBlockAfterCross(JumpPlayer player, Side movement);

    public boolean changeMovement(Entity entity, Side movement) {
        return false;
    }
}

