package com.ktar5.gameengine.core;

import com.badlogic.gdx.Screen;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.util.Updatable;
import lombok.Getter;

@Getter
public abstract class AbstractScreen implements Screen, Updatable {
    protected final CameraBase camera;

    public boolean clearUpdatables = false;

    public AbstractScreen(CameraBase camera) {
        this.camera = camera;
        preInit();
    }

    public void preInit() {

    }

}
