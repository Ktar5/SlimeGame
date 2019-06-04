package com.ktar5.slime.world.events;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.Gate;

public class SetGate implements Runnable {
    public final int x;
    public final int y;
    public final boolean open;

    public SetGate(String xString, String yString, String openClose) {
        x = Integer.valueOf(xString);
        y = Integer.valueOf(yString);
        open = openClose.equalsIgnoreCase("open");
    }

    @Override
    public void run() {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        Gate gate = ((Gate) currentLevel.getGameMap()[x][y]);
        gate.setOpen(true);
    }
}
