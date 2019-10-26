package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.input.ControllerInput;
import com.ktar5.gameengine.input.KInput;
import com.ktar5.gameengine.input.devices.JamPad;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.screens.mainmenu.LevelSelectionScreen;
import com.ktar5.slime.screens.mainmenu.OptionsMenuScreen;
import de.golfgl.gdx.controllers.ControllerMenuStage;

public class PauseWithBlur extends GameState {
    private ControllerMenuStage stage;
    TextButton resumeButton;
    Table mainTable;

    public PauseWithBlur(GameScreen gameScreen) {
        super(gameScreen);

        TextureAtlas atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        Skin skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        SpriteBatch batch = new SpriteBatch();

        stage = new ControllerMenuStage(SlimeGame.getGame().getUiCamera().getViewport(), batch);

        resumeButton = new TextButton("Resume", skin);
        TextButton levelsButton = new TextButton("Levels", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        //Add listeners to buttons
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeState(Running.class);
            }
        });
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SlimeGame.getGame().setScreen(new LevelSelectionScreen());
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

        //Create Table
        mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        mainTable.pad(0, 0, 0, 0);
        mainTable.add(resumeButton).padBottom(10).fill().width(100);
        stage.addFocusableActor(resumeButton);
        mainTable.row();
        mainTable.add(levelsButton).padBottom(10).fill();
        stage.addFocusableActor(levelsButton);
        mainTable.row();
        mainTable.add(optionsButton).padBottom(10).fill();
        stage.addFocusableActor(optionsButton);
        mainTable.row();
        mainTable.add(exitButton).fill();
        stage.addFocusableActor(exitButton);
        stage.addActor(mainTable);

        mainTable.validate();
        stage.setFocusedActor(resumeButton);
        stage.setEscapeActor(resumeButton);
    }

    boolean firstFrame = false;

    @Override
    public void start() {
        EngineManager.get().getConsole().resetInputProcessing();

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage,
                EngineManager.get().getConsole().getInputProcessor(),
                SlimeGame.getGame().getInput());

        Gdx.input.setInputProcessor(inputMultiplexer);
        firstFrame = true;
        framesOfSelectDown = 0;
        stage.setFocusedActor(resumeButton);
        if(Gdx.graphics.getWidth() != currentWidth || Gdx.graphics.getHeight() != currentHeight){
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    int currentWidth = 0, currentHeight = 0;
    @Override
    public void resize(int width, int height) {
        currentWidth = width;
        currentHeight = height;
        // VfxManager manages internal off-screen buffers,
        // which should always match the required viewport (whole screen in our case).
        SlimeGame.getGame().getPostProcess().getVfxManager().resize(width, height);

//        shapeRenderer.getProjectionMatrix().setToOrtho2D(0f, 0f, width, height);
//        shapeRenderer.updateMatrices();

        SlimeGame.getGame().getUiCamera().getViewport().update(width, height);

        SlimeGame.getGame().getUiCamera().getCamera().position.set(
                SlimeGame.getGame().getUiCamera().getCamera().viewportWidth / 2,
                SlimeGame.getGame().getUiCamera().getCamera().viewportHeight / 2, 0);
        SlimeGame.getGame().getUiCamera().getCamera().update();

//        getGameScreen().getCamera().getViewport().update(width, height);
//        getGameScreen().getCamera().getCamera().position.set(
//                getGameScreen().getCamera().getCamera().viewportWidth / 2,
//                getGameScreen().getCamera().getCamera().viewportHeight / 2, 0);
//        getGameScreen().getCamera().getCamera().update();

    }

    int framesOfSelectDown = 0;
    @Override
    public void onUpdate(float dTime) {
        stage.act();

        ControllerInput contInp = EngineManager.get().getControllerInput();
        if (!firstFrame && (KInput.isKeyJustPressed(Input.Keys.ESCAPE) || contInp.isButtonJustPressed(JamPad.START))) {
            changeState(Running.class);
        }

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
        EngineManager.get().getControllerInput().update();
        firstFrame = false;
    }

    @Override
    protected void end() {

    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
        // Begin render to an off-screen buffer.
        SlimeGame.getGame().getPostProcess().getVfxManager().beginCapture();

        Gdx.gl.glClearColor(57 / 255f, 31 / 255f, 58 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SlimeGame.getGame().getLevelHandler().render(batch, dTime);
        getGameScreen().getFrameRate().render(batch, dTime);
        getGameScreen().getVersionInfo().render(batch, dTime);

        // End render to an off-screen buffer.
        SlimeGame.getGame().getPostProcess().getVfxManager().endCapture();

        // Perform effect chain processing and render result to the screen.
        SlimeGame.getGame().getPostProcess().getVfxManager().render();

        stage.draw();
    }
}
