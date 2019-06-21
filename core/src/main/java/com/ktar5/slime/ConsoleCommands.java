package com.ktar5.slime;

import com.ktar5.gameengine.Feature;
import com.ktar5.gameengine.console.CommandExecutor;
import com.ktar5.gameengine.console.LogLevel;
import com.ktar5.gameengine.util.Position;

public class ConsoleCommands extends CommandExecutor {

    public final void singleFrame(boolean value) {
        Feature.SINGLE_FRAME.set(value);
    }

    public final void setFeature(String feature, boolean value) {
        try {
            Feature feat = Feature.valueOf(feature.toUpperCase());
            feat.set(value);
            console.log("Feature: '" + feature.toUpperCase() + "' was changed to " + value, LogLevel.SUCCESS);
        } catch (IllegalArgumentException e) {
            console.log("Feature with name: " + feature.toUpperCase() + " not found!", LogLevel.ERROR);
        }
    }

    public final void zoom(float level) {
        SlimeGame.getGame().gameCamera.getCamera().zoom = level;
        console.log("Set zoom to: " + level);
    }

    public final void getFeature(String feature) {
        try {
            Feature feat = Feature.valueOf(feature.toUpperCase());
            console.log("Feature: '" + feature.toUpperCase() + "' has a value of " + feat.isEnabled(), LogLevel.SUCCESS);
        } catch (IllegalArgumentException e) {
            console.log("Feature with name: " + feature.toUpperCase() + " not found!", LogLevel.ERROR);
        }
    }

    public final void reset() {
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().reset();
    }

    public final void teleport(int x, int y) {
        Position position = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getPosition();
        position.set(position.x+ (x * 16), position.y + (y * 16));
    }

    public final void debugLevel(boolean value) {
        SlimeGame.getGame().getLevelHandler().setLevelDebug(value);
    }

    public final void skipLevel() {
        SlimeGame.getGame().getLevelHandler().advanceLevel();
    }

    public final void ss(int level) {
        try {
            SlimeGame.getGame().getLevelHandler().setLevel(level);
            console.log("Skipping to level: '" + level, LogLevel.SUCCESS);
        } catch (IllegalArgumentException e) {
            console.log("LevelData with name: " + level + " not found. Max = "
                    + (SlimeGame.getGame().getLevelHandler().getLevelCount() - 1), LogLevel.ERROR);
        }
    }

    public final void skipToLevel(int level) {
        try {
            SlimeGame.getGame().getLevelHandler().setLevel(level);
            console.log("Skipping to level: '" + level, LogLevel.SUCCESS);
        } catch (IllegalArgumentException e) {
            console.log("LevelData with name: " + level + " not found. Max = "
                    + (SlimeGame.getGame().getLevelHandler().getLevelCount() - 1), LogLevel.ERROR);
        }
    }

}
