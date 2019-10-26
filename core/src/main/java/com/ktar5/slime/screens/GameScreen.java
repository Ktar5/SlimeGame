package com.ktar5.slime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.gameengine.util.textoverlay.FrameRate;
import com.ktar5.gameengine.util.textoverlay.TextDisplay;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.hotkeys.GeneralHotkeys;
import com.ktar5.slime.misc.VersionInfo;
import com.ktar5.slime.screens.gamestate.GameState;
import com.ktar5.slime.screens.gamestate.PauseWithBlur;
import com.ktar5.slime.screens.gamestate.Running;
import com.ktar5.slime.screens.gamestate.Winning;
import lombok.Getter;
import org.tinylog.Logger;

@Getter
public class GameScreen extends AbstractScreen {
    private SimpleStateMachine<GameState> gameState;
    private TextDisplay frameRate, versionInfo;

    public GameScreen() {
        super(SlimeGame.getGame().getGameCamera());
        Gdx.input.setInputProcessor(new InputMultiplexer(EngineManager.get().getConsole().getInputProcessor(),
                SlimeGame.getGame().getInput()));
        gameState = new SimpleStateMachine<>(
                new Running(this),
                new PauseWithBlur(this),
                new Winning(this));
    }

    @Override
    public void preInit() {
        frameRate = new FrameRate();
        versionInfo = new VersionInfo();
    }

    @Override
    public void update(float dTime) {
//        SlimeGame.frames += 1;
//        System.out.println(SlimeGame.frames);
        gameState.update(dTime);
        GeneralHotkeys.update();
    }

    @Override
    public void show() {
        Logger.debug("Display started for GameScreen");
    }

    @Override
    public void render(float delta) {
        gameState.getCurrent().render(SlimeGame.getGame().getSpriteBatch(), delta);
        KInput.update();
    }

    @Override
    public void pause() {
//        if (gameState.getCurrent().getClass().equals(Winning.class)) {
//            return;
//        }
//        gameState.changeStateAfterUpdate(PauseWithBlur.class);
    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        getGameState().getCurrent().resize(width, height);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
