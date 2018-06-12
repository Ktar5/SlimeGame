package com.ktar5.slime.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StaticCamera extends CameraBase {
    public StaticCamera(OrthographicCamera camera, Viewport viewport) {
        super(camera, viewport);
    }

    @Override
    public void update(float dTime) {
    
    }
}
