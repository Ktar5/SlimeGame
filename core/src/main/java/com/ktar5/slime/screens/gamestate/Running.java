package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.misc.Respawner;
import com.ktar5.slime.screens.GameScreen;

import java.util.Arrays;

public class Running extends GameState {
    private final Respawner respawner;

    public Running(GameScreen gameScreen) {
        super(gameScreen);
        respawner = new Respawner();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void start() {
        getGameScreen().getRenderManager().setRenderables(Arrays.asList(
                SlimeGame.getGame().getLevelHandler(),
                getGameScreen().getFrameRate(),
                getGameScreen().getVersionInfo()
        ));
    }

    @Override
    public void onUpdate(float dTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            changeState(PauseWithBlur2.class);
            return;
        }
        EngineManager.get().getCooldownManager().update(dTime);
        EngineManager.get().getTweenManager().update(dTime);
        EngineManager.get().getCameraBase().update(dTime);
        SlimeGame.getGame().getLevelHandler().update(dTime);
        getGameScreen().getFrameRate().update(dTime);
        getGameScreen().getVersionInfo().update(dTime);
        respawner.update(dTime);
    }

    @Override
    protected void end() {

    }

}
