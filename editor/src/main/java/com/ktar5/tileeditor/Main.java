package com.ktar5.tileeditor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kotcrab.vis.ui.VisUI;
import com.ktar5.tileeditor.scene.Root;
import lombok.Getter;

@Getter
public class Main implements ApplicationListener {
    private static Main instance;
    private Root root;
    private SpriteBatch spriteBatch;

    private TextureRegion selection;


    public static Main getInstance() {
        return instance;
    }

    public void create() {
        instance = this;
        selection = new TextureRegion(new Texture(Gdx.files.internal("textures/selection-highlight.png")));
        VisUI.load();
        root = new Root();
        root.injectFileChooser();
        spriteBatch = new SpriteBatch();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new Input());
        inputMultiplexer.addProcessor(root);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        root.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    float time = 0;

    public static final float MAX_FRAME_TIME = .25f;
    public static final float STEP_TIME = 1f / 60f;

    @Override
    public void render() {
        //Get time since last frame
        float dTime = Gdx.graphics.getDeltaTime();
        //If game too laggy, prevent massive bugs by using a small constant number
        time += Math.min(dTime, MAX_FRAME_TIME);
        //While our time is greater than our fixed step size...
        while (time >= STEP_TIME) {
            time -= STEP_TIME;
            root.act();
        }

        //Because OpenGL needs this.
        //51
        int i = 60;
        Gdx.gl.glClearColor(i / 255f, i / 255f, i / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        root.draw();
    }

    @Override
    public void resize(int width, int height) {
        root.getViewport().update(width, height, true);
    }

}
