package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.entities.player.ShapeState;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableGameTile;
import lombok.Getter;

@Getter
public class PressurePlate extends TriggerableGameTile {
    private static final int onID = 48, offID = 49;

    private boolean pressed = false;

    public PressurePlate(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void reset() {
        pressed = false;
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), offID);
    }

    @Override
    public boolean onCross(Entity entity) {
        if (!pressed) {
            if (entity.isPlayer() && ((JumpPlayer) entity).getShape().equals(ShapeState.TINY)) {
                return false;
            }
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), onID);
            callEvent(Trigger.ON_PASS);

            pressed = true;
        }
        return false;
    }

}
