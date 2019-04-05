package com.ktar5.slime.screens;

import com.ktar5.slime.engine.EngConst;
import com.ktar5.slime.engine.core.AbstractScreen;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.CustomizedRender;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.engine.util.textoverlay.FrameRate;
import com.ktar5.slime.engine.util.textoverlay.TextDisplay;
import com.ktar5.slime.misc.VersionInfo;
import com.ktar5.slime.screens.gamestate.GameState;
import com.ktar5.slime.screens.gamestate.PauseWithBlur2;
import com.ktar5.slime.screens.gamestate.Running;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.Collections;
import java.util.List;

@Getter
public class GameScreen extends AbstractScreen {
    private SimpleStateMachine<GameState> gameState;
    private TextDisplay frameRate, versionInfo;


    public GameScreen() {
        super(EngineManager.get().getCameraBase());
        EngineManager.get().getConsole().resetInputProcessing();
        EngineManager.get().getRenderManager().setCustomizedRender(new CustomizedRender() {
            @Override
            public void postDebug(float dTime) {
                //EngineManager.get().getWorldManager().debug(dTime);
            }
        });
        gameState = new SimpleStateMachine<>(new Running(this), new PauseWithBlur2(this));
    }

    @Override
    public void preInit() {
        frameRate = new FrameRate();
        versionInfo = new VersionInfo();
    }

    @Override
    public List<Renderable> initializeRenderables() {
        return Collections.emptyList();
    }

    @Override
    public void update(float dTime) {
        gameState.update(EngConst.STEP_TIME);
    }

    @Override
    public void initializeUpdatables() {
    }

    @Override
    public void show() {
        Logger.debug("Display started for GameScreen");
    }

    @Override
    public void pause() {
        gameState.changeStateAfterUpdate(PauseWithBlur2.class);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
