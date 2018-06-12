package com.ktar5.slime.engine.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.ktar5.slime.engine.Const;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.debug.Debug;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.utilities.common.collections.DelayedAddList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class RenderManager implements Disposable, Updatable {
    @Getter
    private final SpriteBatch spriteBatch;
    @Getter
    private final ShapeRenderer shapeRenderer;

    private final DelayedAddList<Runnable> actionsAfterNextFrame;
    private List<Renderable> renderables;
    private CustomizedRender customizedRender = CustomizedRender.BLANK;

    public RenderManager() {
        this.actionsAfterNextFrame = new DelayedAddList<>();
        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.renderables = new ArrayList<>();
    }

    public void doOnNextFrame(Runnable runnable) {
        actionsAfterNextFrame.add(runnable);
    }

    public void setRenderables(List<Renderable> newRenderables) {
        doOnNextFrame(() -> renderables = newRenderables);
    }

    public void setCustomizedRender(CustomizedRender newCustomizedRender) {
        doOnNextFrame(() -> customizedRender = newCustomizedRender);
    }

    @Override
    public void update(float dTime) {
        //Because OpenGL needs this.
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(EngineManager.get().getCameraBase().getCamera().combined);
        
        customizedRender.preRender(dTime);

        spriteBatch.begin();
        for (Renderable renderable : renderables) {
            renderable.render(spriteBatch, dTime);
        }
        spriteBatch.end();

        customizedRender.postRender(dTime);
        customizedRender.preDebug(dTime);

        if (Const.DEBUG) {
            shapeRenderer.setProjectionMatrix(EngineManager.get().getCameraBase().getCamera().combined);
            for (Renderable renderable : renderables) {
                if (Debug.shouldDebug(renderable.getClass())) {
                    renderable.debug(dTime);
                }
            }
        }

        customizedRender.postDebug(dTime);

        for (Runnable runnable : actionsAfterNextFrame.getItems()) {
            runnable.run();
        }
        actionsAfterNextFrame.clearItems();
    }

    @Override
    public void dispose() {
        for (Renderable renderable : renderables) {
            if (renderable instanceof Disposable) {
                ((Disposable) renderable).dispose();
            }
        }
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }
}

