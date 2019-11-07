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
import com.ktar5.gameengine.input.KInput;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.slime.data.SlimeGameData;
import com.ktar5.slime.misc.CameraLookAt;
import com.ktar5.slime.misc.ISync;
import com.ktar5.slime.misc.PixelPerfectViewport;
import com.ktar5.slime.misc.PostProcess;
import com.ktar5.slime.platform.AStoreSDK;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.world.level.LevelHandler;
import com.ktar5.slime.world.tiles.HiddenSpikes;
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
    private PostProcess postProcess;

    public StaticCamera uiCamera;
    public CameraLookAt gameCamera;
    private AStoreSDK storeSDK;


    private final ISync sync;

    public SlimeGame(AStoreSDK storeSDK, ISync sync) {
        instance = this;
        this.storeSDK = storeSDK;
        this.sync = sync;
    }

    @Override
    public void render() {
//        sync.sync(Gdx.graphics.getDisplayMode().refreshRate);
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        if(Analytics.enabled){
            Analytics.get().dispose();
        }
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
        Logger.debug("n");
        uiCamera = new StaticCamera(new OrthographicCamera(480, 270));

        OrthographicCamera orthographicCamera = new OrthographicCamera(480, 270);
        viewport = new PixelPerfectViewport(480, 270, orthographicCamera);
        orthographicCamera.update();
//        gameCamera = new CameraFollow(orthographicCamera, viewport, null);
//        gameCamera = new CameraFollow(orthographicCamera, viewport);
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
        //Set analytics to completely disabled for now.
        Analytics.enabled = false;

        if(!Analytics.hasInternet()){
            Analytics.enabled = false;
        }else {
            MongoDBInstance mongoDBInstance = new MongoDBInstance("mongodb+srv://analytics:test@cluster0-k5pjp.mongodb.net/test?retryWrites=true", "test");
            Preferences slimegame = Gdx.app.getPreferences("com.ktar5.slipnslime");
            String build_id = "0.2.0";
            if (DEVELOPER_MODE) {
                build_id = "developer";
            }
            //Increment resets every time you want to reset all the analytics
            Analytics.create(slimegame, mongoDBInstance, build_id, 3, 1);
        }

        //Load everything needed for pre-game
        VisUI.load();
        postProcess = new PostProcess();
        EngineManager.get().getConsole().setDisplayKeyID(Input.Keys.GRAVE);
        Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
        Tween.registerAccessor(HiddenSpikes.class, new HiddenSpikes.SpikesTweenAccessor());
        EngineManager.get().getAnimationLoader().loadAtlas("textures/player/Slime.atlas");
        SlimeGame.getGame().setLevelHandler(new LevelHandler());

        Logger.debug("Starting primary screen");
//        return new NodeLevelSelectionScreen(getGameCamera());
//        return new NewLoadingScreen(SlimeGame.getGame().getUiCamera());
        return new GameScreen();
    }

    @Override
    public SlimeGame getThis() {
        return this;
    }



}