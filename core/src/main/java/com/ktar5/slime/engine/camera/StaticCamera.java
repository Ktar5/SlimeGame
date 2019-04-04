package com.ktar5.slime.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StaticCamera extends CameraBase {
    public StaticCamera(OrthographicCamera camera, Viewport viewport) {
        super(camera, viewport);
    }

    public StaticCamera(OrthographicCamera camera) {
        super(camera, new FitViewport(camera.viewportWidth, camera.viewportHeight, camera));
    }

    @Override
    public void update(float dTime) {

    }
}
