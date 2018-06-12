package com.ktar5.slime.screens;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.AbstractScreen;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.CustomizedRender;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.variables.Constants;
import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.List;

public class GameScreen extends AbstractScreen {

    public GameScreen() {
        super(EngineManager.get().getCameraBase());
        EngineManager.get().getRenderManager().setCustomizedRender(new CustomizedRender() {
            @Override
            public void postDebug(float dTime) {
                //EngineManager.get().getWorldManager().debug(dTime);
            }
        });
    }

    @Override
    public List<Renderable> initilizeRenderables() {
        return Arrays.asList(
                SlimeGame.getGame().getLevelHandler()
        );
    }

    @Override
    public void initializeUpdatables() {
        updatableList.add(EngineManager.get().getCooldownManager());
        updatableList.add(dTime -> EngineManager.get().getTweenManager().update(Constants.FRAME_DT));
        updatableList.add(EngineManager.get().getCameraBase());
        updatableList.add(SlimeGame.getGame().getLevelHandler());
    }

    @Override
    public void show() {
        Logger.debug("Display started for GameScreen");
    }

    @Override
    public void pause() {

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
