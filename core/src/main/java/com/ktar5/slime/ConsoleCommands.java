package com.ktar5.slime;

import com.ktar5.slime.engine.Feature;
import com.ktar5.slime.engine.console.CommandExecutor;
import com.ktar5.slime.engine.console.LogLevel;
import com.ktar5.slime.player.states.Respawn;
import com.ktar5.slime.world.level.LevelRef;

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

    }

    public final void teleport(int x, int y) {

    }

    public final void debugLevel(boolean value){
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().setLevelDebug(value);
    }

    public final void restartLevel() {
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getPlayerState().changeStateAfterUpdate(Respawn.class);
    }

    public final void skipLevel() {
        int ordinal = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getLevelRef().ordinal();
        ordinal += 1;
        if (ordinal > LevelRef.values().length - 1) {
            ordinal = 0;
        }
        SlimeGame.getGame().getLevelHandler().setLevel(LevelRef.values()[ordinal]);
    }

    public final void skipToLevel(int level) {
        try {
            LevelRef levelObj = LevelRef.values()[level];
            console.log("Skipping to level: '" + levelObj.name(), LogLevel.SUCCESS);
            SlimeGame.getGame().getLevelHandler().setLevel(levelObj);
        } catch (IllegalArgumentException e) {
            console.log("Level with name: " + level + " not found. Max = " + (LevelRef.values().length - 1),
                    LogLevel.ERROR);
        }
    }

}
