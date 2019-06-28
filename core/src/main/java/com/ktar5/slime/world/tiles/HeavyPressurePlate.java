package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.Rectangle;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableGameTile;

import java.util.List;

public class HeavyPressurePlate extends TriggerableGameTile {
    private static final int onID = 34, offID = 35;
    private static final Rectangle hitbox = new Rectangle(16, 16);

    private boolean pressed = false;

    public HeavyPressurePlate(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void processProperty(MapProperties properties) {
        super.processProperty(properties);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void tick() {
        JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
        if (player.isTouching(hitbox, (x * 16) + 8, (y * 16) + 8)) {
            if (!pressed) {
                enable();
                pressed = true;
            }
            return;
        } else {
            List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
            for (Entity entity : entities) {
                if (entity instanceof GameEntity && ((GameEntity) entity).isTouching(hitbox, (x * 16) + 8, (y * 16) + 8)) {
                    if (!pressed) {
                        enable();
                        pressed = true;
                    }
                    return;
                }
            }
        }

        if (pressed) {
            disable();
            pressed = false;
        }
    }

    @Override
    public void reset() {
        pressed = false;
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), offID);
    }

    public void enable() {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), onID);
        callEvent(Trigger.ON_ENABLE);
    }

    public void disable() {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), offID);
        callEvent(Trigger.ON_DISABLE);
    }

}
