package com.ktar5.slime.engine.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ktar5.slime.engine.util.Updatable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public abstract class CameraBase implements Updatable {
    protected final OrthographicCamera camera;
    protected final Viewport viewport;
}
