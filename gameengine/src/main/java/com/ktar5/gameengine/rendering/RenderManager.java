package com.ktar5.gameengine.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.util.Updatable;
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
        Gdx.gl.glClearColor(168 / 255f, 118 / 255f, 86 / 255f, 1);
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

        if (EngConst.DEBUG) {
            shapeRenderer.setProjectionMatrix(EngineManager.get().getCameraBase().getCamera().combined);
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            for (Renderable renderable : renderables) {
                renderable.debug(dTime);
//                if (Debug.shouldDebug(renderable.getClass())) {
//                    renderable.debug(dTime);
//                }
            }
            shapeRenderer.end();
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

