package com.ktar5.slime.engine.core;

import com.badlogic.gdx.Screen;
import com.ktar5.slime.engine.EngConst;
import com.ktar5.slime.engine.camera.CameraBase;
import com.ktar5.slime.engine.rendering.RenderManager;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.util.Updatable;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractScreen implements Screen, Updatable {
    @Getter(value = AccessLevel.PRIVATE)
    private final List<Updatable> updatableList;
    private final List<Updatable> addedUpdatables;
    protected final RenderManager renderManager;
    protected final CameraBase camera;

    public boolean clearUpdatables = false;

    public AbstractScreen(CameraBase camera) {
        this.camera = camera;
        this.updatableList = new ArrayList<>();
        this.addedUpdatables = new ArrayList<>();
        this.renderManager = EngineManager.get().getRenderManager();
        if (renderManager == null) {
            throw new RuntimeException("RenderManager cannot be null");
        }
        preInit();
        this.renderManager.setRenderables(initializeRenderables());
        initializeUpdatables();
    }

    public void preInit() {

    }

    public abstract List<Renderable> initializeRenderables();

    public abstract void initializeUpdatables();

    @Override
    public void dispose() {
        renderManager.dispose();
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
    public void render(float delta) {
        //Do nothing?
    }

    @Override
    public void resize(int width, int height) {
        //Nothing
    }

    public void addUpdatable(Updatable updatable){
        addedUpdatables.add(updatable);
    }

    @Override
    public void update(float dTime) {
        if(clearUpdatables){
            updatableList.clear();
            clearUpdatables = false;
        }
        updatableList.addAll(addedUpdatables);
        addedUpdatables.clear();
        for (Updatable updatable : updatableList) {
            //Update all updatables, in order
            updatable.update(EngConst.STEP_TIME);
        }
    }
}
