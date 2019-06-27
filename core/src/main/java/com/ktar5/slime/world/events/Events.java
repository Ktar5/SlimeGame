package com.ktar5.slime.world.events;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.Gate;

public final class Events {

    public static void setGate(String xString, String yString, String openClose) {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        int x = Integer.valueOf(xString);
        int y = currentLevel.getHeight() - 1 - Integer.valueOf(yString);
        boolean open = openClose.equalsIgnoreCase("open");

        Gate gate = ((Gate) currentLevel.getGameMap()[x][y]);
        gate.setOpen(open);
    }

    public static void setGraphic(String xString, String yString, String layer, String tilesetID) {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        int x = Integer.valueOf(xString);
        int y = currentLevel.getHeight() - 1 - Integer.valueOf(yString);
        int tileID = Integer.valueOf(tilesetID);

        //TODO
        int previousTile = currentLevel.setGraphic(x, y, "GameplayImages", layer, tileID);
        currentLevel.addEdit(x, y, currentLevel.getRenderMap().getLayers().getIndex(layer), previousTile);
    }

}
