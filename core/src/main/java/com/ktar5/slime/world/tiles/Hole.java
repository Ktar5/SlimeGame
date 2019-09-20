package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.hero.HeroEntity;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Hole extends WholeGameTile {
    boolean filled = false;

    public Hole(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        filled = false;

//        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
//        currentLevel.setGraphic(x, y, "GameplayImages", currentLevel.getGameplayArtLayer(), 221);
    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public boolean onCross(Entity entity) {
        if (filled) {
            return false;
        }

        if (entity.isPlayer()) {
            ((JumpPlayer) entity).kill("hole");
        } else if (entity instanceof Box) {
            filled = true;
            SlimeGame.getGame().doOnNextFrame(() -> {
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(entity);
            });
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            currentLevel.addEdit(x, y, currentLevel.getGameplayArtLayerIndex(),
                    currentLevel.getCurrentID(x, y, currentLevel.getGameplayArtLayer()));
            currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), 161);
        } else if (entity instanceof HeroEntity){
            ((HeroEntity) entity).kill();
        }

        return true;
    }
}
