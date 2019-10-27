package com.ktar5.gameengine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ktar5.gameengine.util.Updatable;

public abstract class CameraBase implements Updatable {
    protected final OrthographicCamera camera;
    protected final Viewport viewport;

    public CameraBase(OrthographicCamera camera, Viewport viewport) {
        this.camera = camera;
        this.viewport = viewport;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public Viewport getViewport() {
        return this.viewport;
    }
}
