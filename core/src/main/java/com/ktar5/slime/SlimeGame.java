package com.ktar5.slime;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ktar5.slime.engine.camera.CameraBase;
import com.ktar5.slime.engine.camera.CameraFollow;
import com.ktar5.slime.engine.console.CommandExecutor;
import com.ktar5.slime.engine.core.AbstractGame;
import com.ktar5.slime.engine.core.AbstractScreen;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.tweenengine.Tween;
import com.ktar5.slime.entities.EntityTweenAccessor;
import com.ktar5.slime.screens.LoadingScreen;
import com.ktar5.slime.world.level.LevelHandler;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SlimeGame extends AbstractGame<SlimeGame> {
    private static SlimeGame instance;

    //public final GameAnalytics gameAnalytics;
    private float zoomLevel = 4f;
    private FitViewport viewport;
    @Setter
    private LevelHandler levelHandler;

    public SlimeGame() {
        instance = this;
    }

    public static SlimeGame getGame() {
        return instance;
    }

    @Override
    public void initialize() {
        this.engineManager.getConsole().setDisplayKeyID(Input.Keys.GRAVE);
        Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
    }

    @Override
    protected CameraBase initializeCameraBase() {
        OrthographicCamera orthographicCamera = new OrthographicCamera(480, 270);
        viewport = new FitViewport(480, 270, orthographicCamera);
        orthographicCamera.update();
        return new CameraFollow(orthographicCamera, viewport, null);
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
        return new LoadingScreen(EngineManager.get().getCameraBase());
    }

    @Override
    public SlimeGame getThis() {
        return this;
    }

}