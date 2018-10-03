package com.ktar5.slime.screens;

import com.ktar5.slime.engine.camera.CameraBase;
import com.ktar5.slime.engine.core.AbstractScreen;
import com.ktar5.slime.engine.rendering.Renderable;

import java.util.Collections;
import java.util.List;

public class MenuScreen extends AbstractScreen {
    public MenuScreen(CameraBase camera) {
        super(camera);
    }

    @Override
    public List<Renderable> initializeRenderables() {
        return Collections.emptyList();
    }

    @Override
    public void initializeUpdatables() {

    }

    @Override
    public void show() {

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
    public void dispose() {

    }
}
