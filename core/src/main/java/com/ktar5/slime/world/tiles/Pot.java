package com.ktar5.slime.world.tiles;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Pot extends WholeGameTile {
    public boolean crumbled = false;

    public Pot(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        crumbled = false;
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), 111);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return crumbled;
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {
        if (!crumbled) {
            crumbled = true;
            //TODO do arrows work with this?
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), 146);
        }
    }

}