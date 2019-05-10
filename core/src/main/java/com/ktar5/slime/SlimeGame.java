package com.ktar5.slime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.ui.VisUI;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.analytics.MongoDBInstance;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.camera.CameraFollow;
import com.ktar5.gameengine.console.CommandExecutor;
import com.ktar5.gameengine.core.AbstractGame;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.EntityTweenAccessor;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.slime.misc.PixelPerfectViewport;
import com.ktar5.slime.screens.LoadingScreen;
import com.ktar5.slime.world.level.LevelHandler;
import com.ktar5.slime.world.tiles.RetractingSpikes;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SlimeGame extends AbstractGame<SlimeGame> {
    private static SlimeGame instance;

    //public final GameAnalytics gameAnalytics;
    private float zoomLevel = 4f;
    private PixelPerfectViewport viewport;
    @Setter
    private LevelHandler levelHandler;

//    private GameAnalytics gameAnalytics;

    public SlimeGame() {
        instance = this;
    }

    @Override
    public void dispose() {
        super.dispose();
        Analytics.get().dispose();
    }

    public static SlimeGame getGame() {
//        if(instance.gameAnalytics == null){
//            instance.initGameAnalytics();
//        }
        return instance;
    }

    @Override
    public void pause() {
        super.pause();
//        gameAnalytics.closeSession();
    }

    @Override
    public void resume() {
        super.resume();
//        gameAnalytics.startSession();
    }

    @Override
    public void initialize() {
        VisUI.load();
        this.engineManager.getConsole().setDisplayKeyID(Input.Keys.GRAVE);
        Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
        Tween.registerAccessor(RetractingSpikes.class, new RetractingSpikes.SpikesTweenAccessor());
    }

    @Override
    protected CameraBase initializeCameraBase() {
        OrthographicCamera orthographicCamera = new OrthographicCamera(480, 270);
        viewport = new PixelPerfectViewport(480, 270, orthographicCamera);
        orthographicCamera.update();
        return new CameraFollow(orthographicCamera, viewport, null);
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
        MongoDBInstance mongoDBInstance = new MongoDBInstance("mongodb+srv://analytics:test@cluster0-k5pjp.mongodb.net/test?retryWrites=true", "test");
        Preferences slimegame = Gdx.app.getPreferences("com.ktar5.slimegame");
        Analytics.create(slimegame, mongoDBInstance,"debug");
        return new LoadingScreen(EngineManager.get().getCameraBase());
    }

    @Override
    public SlimeGame getThis() {
        return this;
    }

    String gameVersionNumber = "0.5.0";
    boolean devMode = true;

//    protected void initGameAnalytics() {
//        gameAnalytics = new GameAnalytics();
//        gameAnalytics.setPlatformVersionString("1");
//
//        Preferences prefs = Gdx.app.getPreferences("TESTING");
//        gameAnalytics.setPrefs(prefs);
//        gameAnalytics.setPlatform(GameAnalytics.Platform.Windows);
//        gameAnalytics.setGameBuildNumber(devMode ? "debug" : gameVersionNumber);
//
////        gameAnalytics.setPrefs();
//        gameAnalytics.setGameKey("58ffd0af688f2a76303fb092abb994ee");
//        gameAnalytics.setGameSecretKey("16dfb7bb5c2a7e50d8ae88876f965691b96b11f5");
//        gameAnalytics.startSession();
//    }

}