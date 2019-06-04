package com.ktar5.slime.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.util.Updatable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelFailEvent;

public class Respawner implements Updatable {
    @Override
    public void update(float dTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            Analytics.addEvent(new LevelFailEvent(SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer(), "reset"));
            SlimeGame.getGame().getLevelHandler().resetLevel();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            SlimeGame.getGame().getLevelHandler().toggleDebug();
            EngConst.DEBUG = !EngConst.DEBUG;
        }
    }
}
