package com.ktar5.slime.screens.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ktar5.gameengine.core.AbstractScreen;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.input.ControllerInput;
import com.ktar5.gameengine.input.devices.JamPad;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.hotkeys.GeneralHotkeys;
import com.ktar5.slime.screens.GameScreen;
import de.golfgl.gdx.controllers.ControllerMenuStage;

public class MainMenuScreen extends AbstractScreen {
    protected ControllerMenuStage stage;
    private TextureAtlas atlas;
    protected Skin skin;

    public MainMenuScreen() {
        super(SlimeGame.getGame().getUiCamera());

        atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new ControllerMenuStage (getCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());

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
        mainTable.add(playButton).pad(0, 0, 0, 0).fill();
        stage.addFocusableActor(playButton);
        mainTable.row();
        mainTable.add(levelsButton).pad(10, 0, 0, 0).fill();
        stage.addFocusableActor(levelsButton);
        mainTable.row();
        mainTable.add(optionsButton).pad(10, 0, 0, 0).fill();
        stage.addFocusableActor(optionsButton);
        mainTable.row();
        mainTable.add(exitButton).pad(10, 0, 0, 0).width(100f).fill();
        stage.addFocusableActor(exitButton);

        //Add table to stage
        stage.addActor(new Actor() {
            Texture texture = EngineManager.get().getAnimationLoader().getTexture("textures/MenuBG.png");

            @Override
            public void draw(Batch batch, float parentAlpha) {
                stage.getBatch().draw(texture, 0, 0, 480, 270);
            }
        });
        stage.addActor(mainTable);
//        stage.addFocusableActor(mainTable);
        mainTable.validate();
        stage.setFocusedActor(playButton);
    }


    @Override
    public void show() {
        //Stage should control input:
        //new InputMultiplexer(stage, SlimeGame.getGame().getInput())
        Gdx.input.setInputProcessor(stage);
        framesOfSelectDown = 0;
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

    int framesOfSelectDown = 0;
    @Override
    public void update(float dTime) {
        ControllerInput contInp = EngineManager.get().getControllerInput();
        if(contInp.isButtonJustPressed(JamPad.DPAD_DOWN)){
            stage.keyDown(Input.Keys.DOWN);
        } else if(contInp.isButtonJustPressed(JamPad.DPAD_UP)){
            stage.keyDown(Input.Keys.UP);
        }else if(contInp.isButtonJustPressed(JamPad.A)) {
            stage.keyDown(Input.Keys.ENTER);
            framesOfSelectDown = 1;
        }

        if(framesOfSelectDown >= 4){
            framesOfSelectDown = 0;
            stage.keyUp(Input.Keys.ENTER);
        }else if(framesOfSelectDown > 0){
            framesOfSelectDown += 1;
        }
        contInp.update();

        GeneralHotkeys.update();

    }
}
