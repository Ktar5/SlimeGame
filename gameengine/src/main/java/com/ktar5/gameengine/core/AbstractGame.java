package com.ktar5.gameengine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.Feature;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.console.CommandExecutor;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import com.ktar5.utilities.common.collections.DelayedAddList;
import lombok.Getter;

public abstract class AbstractGame<G extends AbstractGame<G>> implements ApplicationListener {
    protected AbstractScreen screen;

    protected EngineManager<G> engineManager;

    @Getter
    private SpriteBatch spriteBatch;
    @Getter
    private ShapeRenderer shapeRenderer;
    private DelayedAddList<Runnable> actionsAfterNextFrame;

    public static int RED = 57, GREEN = 31, BLUE = 58;

    @Override
    public final void create() {
        this.actionsAfterNextFrame = new DelayedAddList<>();
        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        engineManager = EngineManager.initialize(getThis());
        initialize();
        setScreen(getStartingScreen());
    }

    protected abstract CameraBase initializeCameraBase();

    protected abstract AbstractScreen getStartingScreen();

    public abstract void initialize();

    public abstract G getThis();

    @Override
    @CallSuper
    public void dispose() {
        if (screen != null) {
            screen.hide();
            screen.dispose();
        }
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }

    @Override
    public void pause() {
        if (screen != null) {
            screen.pause();
        }
    }

    @Override
    public void resume() {
        if (screen != null) screen.resume();
    }

    private float time = 0;

    @Override
    public void render() {
        //Get time since last frame
        float dTime = Gdx.graphics.getDeltaTime();
//        System.out.println(dTime);
        if (Feature.SINGLE_FRAME.isDisabled()) {
            //If game too laggy, prevent massive bugs by using a small constant number
            time += Math.min(dTime, EngConst.MAX_FRAME_TIME);
            //While our time is greater than our fixed step size...
            while (time >= EngConst.STEP_TIME) {
                time -= EngConst.STEP_TIME;
                //Update the camera
                screen.update(dTime);
                screen.getCamera().getCamera().update();
            }
        } else {
            screen.getCamera().getCamera().update();

            screen.update(dTime);
        }


        Gdx.gl.glClearColor(RED / 255f, GREEN / 255f, BLUE / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(EngineManager.get().getCameraBase().getCamera().combined);
        spriteBatch.begin();
        screen.render(dTime);
        spriteBatch.end();

        EngineManager.get().getConsole().draw();

        for (Runnable runnable : actionsAfterNextFrame.getItems()) {
            runnable.run();
        }
        actionsAfterNextFrame.clearItems();
    }

    @Override
    public void resize(int width, int height) {
        if (screen != null) screen.resize(width, height);
    }

    /**
     * Sets the current screen. {@link AbstractScreen#hide()} is called on any old screen, and {@link AbstractScreen#show()} is called on the new
     * screen, if any.
     *
     * @param screen may be {@code null}
     */
    public void setScreen(AbstractScreen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * @return the currently active {@link AbstractScreen}.
     */
    public AbstractScreen getScreen() {
        return screen;
    }

    public abstract CommandExecutor getCommandExecutor();

    public void doOnNextFrame(Runnable runnable) {
        actionsAfterNextFrame.add(runnable);
    }
}
