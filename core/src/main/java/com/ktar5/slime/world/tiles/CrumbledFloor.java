package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class CrumbledFloor extends WholeGameTile {
    public boolean crumbled = false;
    boolean filled = false;

    public CrumbledFloor(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        crumbled = false;
        filled = false;
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), 119);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public boolean onCross(Entity entity) {
        if (crumbled) {
            if (filled) {
                return false;
            } else if (entity instanceof Box) {
                filled = true;
                SlimeGame.getGame().doOnNextFrame(() -> {
                    SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(entity);
                });
                LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
                currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), 221);
            } else if (entity.isPlayer()) {
                ((JumpPlayer) entity).kill("crumbled_floor");
                return true;
            }
            return true;
        } else {
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), 26);
            crumbled = true;
            return false;
        }
    }
}
