package com.ktar5.slime;

import com.ktar5.slime.engine.Feature;
import com.ktar5.slime.engine.console.CommandExecutor;
import com.ktar5.slime.engine.console.LogLevel;
import com.ktar5.slime.entities.player.states.Respawn;

public class ConsoleCommands extends CommandExecutor {

    public final void setFeature(String feature, boolean value) {
        try {
            Feature feat = Feature.valueOf(feature.toUpperCase());
            feat.set(value);
            console.log("Feature: '" + feature.toUpperCase() + "' was changed to " + value, LogLevel.SUCCESS);
        } catch (IllegalArgumentException e) {
            console.log("Feature with name: " + feature.toUpperCase() + " not found!", LogLevel.ERROR);
        }
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
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getPosition().set(x, y);
    }

    public final void debugLevel(boolean value) {
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().setLevelDebug(value);
    }

    public final void restartLevel() {
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getEntityState().changeStateAfterUpdate(Respawn.class);
    }

    public final void respawn() {
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getEntityState().changeStateAfterUpdate(Respawn.class);
    }

    public final void skipLevel() {
        SlimeGame.getGame().getLevelHandler().advanceLevel();
    }

    public final void skipToLevel(int level) {
        try {
            SlimeGame.getGame().getLevelHandler().setLevel(level);
            console.log("Skipping to level: '" + level, LogLevel.SUCCESS);
        } catch (IllegalArgumentException e) {
            console.log("Level with name: " + level + " not found. Max = "
                    + (SlimeGame.getGame().getLevelHandler().getLevelCount() - 1), LogLevel.ERROR);
        }
    }

}
