package com.ktar5.slime.screens.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.hotkeys.GeneralHotkeys;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NodeLevelSelectionScreen extends AbstractScreen {
    private Stage stage;
    private World world;

    public NodeLevelSelectionScreen(CameraBase camera) {
        super(camera);

        loadWorld(new File("D:\\GameDev\\Projects\\Personal\\SlimeJump\\assets\\maps\\levelselect\\SCENETEST.json"));

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(getCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());
    }

    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, SlimeGame.getGame().getInput(), EngineManager.get().getConsole().getInputProcessor()));

        stage.getActors().clear();
        stage.clear();

        //Add table to stage
        stage.addActor(new Actor() {
            Texture texture = EngineManager.get().getAnimationLoader().getTexture("textures/MenuBG.png");

            @Override
            public void draw(Batch batch, float parentAlpha) {
                stage.getBatch().draw(texture, 0, 0, 480, 270);
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        SlimeGame.getGame().getSpriteBatch().end();
        stage.draw();
        SlimeGame.getGame().getSpriteBatch().begin();
        world.render(SlimeGame.getGame().getSpriteBatch(), delta);
    }

    @Override
    public void resize(int width, int height) {
        getCamera().getViewport().update(width, height);
        getCamera().getCamera().position.set(getCamera().getCamera().viewportWidth / 2, getCamera().getCamera().viewportHeight / 2, 0);
        getCamera().getCamera().update();
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

    @Override
    public void update(float dTime) {
        world.getWorldPlayer().update(dTime);

        GeneralHotkeys.update();
    }


    public void loadWorld(File loaderFile) {
        String data = readFileAsString(loaderFile);
        if (data == null || data.isEmpty()) {
            return;
        }

        world = new World(new JSONObject(data));
    }

    public static String readFileAsString(File file) {
        StringBuilder fileData = new StringBuilder();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                fileData.append(String.valueOf(buf, 0, numRead));
            }
            reader.close();
            fr.close();
        } catch (IOException e) {
            Logger.info(e);
            return null;
        }
        return fileData.toString();
    }
}
