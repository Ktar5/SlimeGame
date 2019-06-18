package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.hotkeys.RunningHotkeys;
import com.ktar5.slime.screens.GameScreen;

public class Running extends GameState {

    public Running(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void resize(int width, int height) {
//        getGameScreen().getCamera().getViewport().update(width, height);
    }

    @Override
    public void start() {
        Gdx.input.setInputProcessor(null);
        EngineManager.get().getConsole().resetInputProcessing();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(EngineManager.get().getConsole().getInputProcessor(), SlimeGame.getGame().getInput());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void onUpdate(float dTime) {
//        System.out.println("Updating running state");
        if (KInput.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESCAPE");
            changeState(PauseWithBlur.class);
            return;
        }
        RunningHotkeys.update();
        EngineManager.get().getCooldownManager().update(dTime);
        EngineManager.get().getTweenManager().update(dTime);
        EngineManager.get().getCameraBase().update(dTime);
        SlimeGame.getGame().getLevelHandler().update(dTime);
        getGameScreen().getFrameRate().update(dTime);
        getGameScreen().getVersionInfo().update(dTime);
    }

    @Override
    protected void end() {

    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
        SlimeGame.getGame().getLevelHandler().render(batch, dTime);
        getGameScreen().getFrameRate().render(batch, dTime);
        getGameScreen().getVersionInfo().render(batch, dTime);
    }
}
