package com.ktar5.slime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.ui.VisUI;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.analytics.MongoDBInstance;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.camera.StaticCamera;
import com.ktar5.gameengine.console.CommandExecutor;
import com.ktar5.gameengine.core.AbstractGame;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.EntityTweenAccessor;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.slime.data.SlimeGameData;
import com.ktar5.slime.misc.CameraLookAt;
import com.ktar5.slime.misc.PixelPerfectViewport;
import com.ktar5.slime.screens.loading.NewLoadingScreen;
import com.ktar5.slime.world.level.LevelHandler;
import com.ktar5.slime.world.tiles.RetractingSpikes;
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


        //Load everything needed for pre-game
        VisUI.load();
        EngineManager.get().getConsole().setDisplayKeyID(Input.Keys.GRAVE);
        Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
        Tween.registerAccessor(RetractingSpikes.class, new RetractingSpikes.SpikesTweenAccessor());
        MongoDBInstance mongoDBInstance = new MongoDBInstance("mongodb+srv://analytics:test@cluster0-k5pjp.mongodb.net/test?retryWrites=true", "test");
        Preferences slimegame = Gdx.app.getPreferences("com.ktar5.slimegame");
        String build_id = "0.1.0";
        if (DEVELOPER_MODE) {
            build_id = "developer";
        }
        Analytics.create(slimegame, mongoDBInstance, build_id, 2, 3);
        EngineManager.get().getAnimationLoader().loadAtlas("textures/player/Slime.atlas");
        SlimeGame.getGame().setLevelHandler(new LevelHandler());




        Logger.debug("Starting primary screen");
        return new NewLoadingScreen(SlimeGame.getGame().getUiCamera());
    }

    @Override
    public SlimeGame getThis() {
        return this;
    }

}