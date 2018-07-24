package com.ktar5.slime;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ktar5.slime.engine.camera.CameraBase;
import com.ktar5.slime.engine.camera.CameraFollow;
import com.ktar5.slime.engine.console.CommandExecutor;
import com.ktar5.slime.engine.core.AbstractGame;
import com.ktar5.slime.engine.core.AbstractScreen;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.variables.Constants;
import com.ktar5.slime.world.LevelHandler;
import com.ktar5.slime.world.Levels;
import lombok.Getter;

@Getter
public class SlimeGame extends AbstractGame<SlimeGame> {
    private static SlimeGame instance;
    
    //public final GameAnalytics gameAnalytics;
    private float zoomLevel = 4f;
    private FitViewport viewport;
    private AssetManager manager = new AssetManager();
    private LevelHandler levelHandler;
    
    public SlimeGame() {
        instance = this;
    }
    
    @Override
    public void initialize() {
        this.engineManager.getConsole().setDisplayKeyID(Input.Keys.GRAVE);
        
        levelHandler = new LevelHandler(Levels.TEST);
    }
    
    @Override
    protected CameraBase initializeCameraBase() {
        OrthographicCamera orthographicCamera = new OrthographicCamera(480 / Constants.CAMERA_SCALE, 270 / Constants.CAMERA_SCALE);
        viewport = new FitViewport(480 / Constants.CAMERA_SCALE, 270 / Constants.CAMERA_SCALE, orthographicCamera);
        orthographicCamera.update();
        return new CameraFollow(orthographicCamera, viewport, null);
        //return new CameraMove(orthographicCamera, viewport);
    }
    
    @Override
    public void resize(int width, int height) {
        EngineManager.get().getCameraBase().getCamera().update();
        super.resize(width, height);
    }
    
    @Override
    public CommandExecutor getCommandExecutor() {
        return new ConsoleCommands();
    }
    
    @Override
    protected AbstractScreen getStartingScreen() {
        return new GameScreen();
    }
    
    @Override
    public SlimeGame getThis() {
        return this;
    }
    
    public static SlimeGame getGame() {
        return instance;
    }
    
}