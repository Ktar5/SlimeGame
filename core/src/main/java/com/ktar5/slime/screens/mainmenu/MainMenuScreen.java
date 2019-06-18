package com.ktar5.slime.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.hotkeys.GeneralHotkeys;
import com.ktar5.slime.screens.GameScreen;

public class MainMenuScreen extends AbstractScreen {
    protected Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;

    public MainMenuScreen() {
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
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, SlimeGame.getGame().getInput()));

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.bottom().left();
        mainTable.pad(0, 25, 25, 0);

        //Create buttons
        TextButton playButton = new TextButton("Play", skin);
        TextButton levelsButton = new TextButton("Levels", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        //Add listeners to buttons
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().setScreen(new LevelSelectionScreen());
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().setScreen(new GameScreen());
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().setScreen(new OptionsMenuScreen());
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        mainTable.add(playButton).pad(0, 0, 0, 0);
        mainTable.row();
        mainTable.add(levelsButton).pad(10, 0, 0, 0);
        mainTable.row();
        mainTable.add(optionsButton).pad(10, 0, 0, 0);
        mainTable.row();
        mainTable.add(exitButton).pad(10, 0, 0, 0);

        //Add table to stage
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
        GeneralHotkeys.update();
        KInput.update();
    }
}
