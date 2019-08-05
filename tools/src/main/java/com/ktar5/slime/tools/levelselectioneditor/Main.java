package com.ktar5.slime.tools.levelselectioneditor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.ktar5.slime.tools.levelselectioneditor.input.Input;
import com.ktar5.slime.tools.levelselectioneditor.ui.MainStage;

public class Main implements ApplicationListener {
    private static Main instance;
    public SceneManager manager;
    public MainStage mainStage;

    @Override
    public void create() {
        instance = this;
        manager = new SceneManager();
        mainStage = new MainStage();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new Input());
        inputMultiplexer.addProcessor(mainStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        int i = 60;
        Gdx.gl.glClearColor(i / 255f, i / 255f, i / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.act();
        mainStage.draw();
    }

    public static Main getInstance() {
        return instance;
    }

    //region stuff I don't need
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
    }
//endregion

}
