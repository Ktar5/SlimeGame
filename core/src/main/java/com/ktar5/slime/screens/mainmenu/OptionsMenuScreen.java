package com.ktar5.slime.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.slime.SlimeGame;

public class OptionsMenuScreen extends AbstractScreen {
    boolean dirty = false; //TODO make apply button grey if clean

    protected Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;

    public OptionsMenuScreen() {
        super(SlimeGame.getGame().getUiCamera());

        atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(getCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center().bottom().padBottom(25);

        //Create buttons
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        TextButton applyButton = new TextButton("Apply Changes", skin);
        applyButton.setDisabled(true);

        /*
        CREATE MENU
         */
        Table optionsTable = new Table();
        optionsTable.center();
        optionsTable.padBottom(15);

        TextButton fullscreenButton = new TextButton("Fullscreen", skin, "toggle");
        fullscreenButton.setChecked(SlimeGame.getGame().getData().fullscreen);
        fullscreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().getData().fullscreen = !SlimeGame.getGame().getData().fullscreen;
                if (SlimeGame.getGame().getData().fullscreen) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    Gdx.graphics.setWindowedMode(960, 540);
                }
                applyButton.setDisabled(false);
            }
        });

        optionsTable.add(fullscreenButton).center().row();

        Table bottomButtonRow = new Table();
        //Add listeners to buttons
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().setScreen(new MainMenuScreen());
            }
        });
        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().getData().saveGame();
                applyButton.setDisabled(true);
            }
        });

        bottomButtonRow.add(backButton).pad(0, 0, 0, 0);
        bottomButtonRow.add(applyButton).pad(0, 25, 0, 0);


        mainTable.add(optionsTable).row();
        mainTable.debugAll();
        mainTable.add(bottomButtonRow).bottom();

        //Add background
        stage.addActor(new Actor() {
            Texture texture = EngineManager.get().getAnimationLoader().getTexture("textures/MenuBG.png");

            @Override
            public void draw(Batch batch, float parentAlpha) {
                stage.getBatch().draw(texture, 0, 0, 480, 270);
            }
        });

        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        SlimeGame.getGame().getSpriteBatch().end();
        stage.draw();
        SlimeGame.getGame().getSpriteBatch().begin();
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
        skin.dispose();
        atlas.dispose();
    }

    @Override
    public void update(float dTime) {

    }
}
