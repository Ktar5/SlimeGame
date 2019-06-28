package com.ktar5.slime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.camera.StaticCamera;
import com.ktar5.gameengine.console.CommandExecutor;
import com.ktar5.gameengine.core.AbstractGame;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.slime.data.SlimeGameData;
import com.ktar5.slime.misc.CameraLookAt;
import com.ktar5.slime.misc.PixelPerfectViewport;
import com.ktar5.slime.screens.loading.NewLoadingScreen;
import com.ktar5.slime.world.level.LevelHandler;
import lombok.Getter;
import lombok.Setter;
import org.tinylog.Logger;

@Getter
public class SlimeGame extends AbstractGame<SlimeGame> {
    public static final boolean DEVELOPER_MODE = true;

    private static SlimeGame instance;

    public static long frames = 0;
    private PixelPerfectViewport viewport;

    private KInput input;
    private SlimeGameData data;
    @Setter private LevelHandler levelHandler;

    public StaticCamera uiCamera;
    public CameraLookAt gameCamera;


    public SlimeGame() {
        instance = this;
    }

    @Override
    public void dispose() {
        super.dispose();
        Analytics.get().dispose();
    }

    public static SlimeGame getGame() {
        return instance;
    }

    @Override
    public void initialize() {
        data = new SlimeGameData();
        input = new KInput();
        if(data.fullscreen){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    @Override
    protected CameraBase initializeCameraBase() {
        uiCamera = new StaticCamera(new OrthographicCamera(480, 270));

        OrthographicCamera orthographicCamera = new OrthographicCamera(480, 270);
        viewport = new PixelPerfectViewport(480, 270, orthographicCamera);
        orthographicCamera.update();
//        gameCamera = new CameraFollow(orthographicCamera, viewport, null);
        gameCamera = new CameraLookAt(orthographicCamera, viewport);
        return gameCamera;

    }

    @Override
    public void resize(int width, int height) {
        EngineManager.get().getCameraBase().getCamera().update();
        EngineManager.get().getCameraBase().getViewport().update(width, height);
        EngineManager.get().getConsole().refresh();
        super.resize(width, height);
    }

    @Override
    public CommandExecutor getCommandExecutor() {
        return new ConsoleCommands();
    }

    @Override
    protected AbstractScreen getStartingScreen() {
        Logger.debug("Starting primary screen");
        return new NewLoadingScreen(SlimeGame.getGame().getUiCamera());
    }

    @Override
    public SlimeGame getThis() {
        return this;
    }

}