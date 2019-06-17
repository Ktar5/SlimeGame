package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.camera.StaticCamera;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.postprocessing.PostProcessor;
import com.ktar5.gameengine.postprocessing.effects.Vignette;
import com.ktar5.gameengine.postprocessing.filters.Blur;
import com.ktar5.gameengine.postprocessing.utils.ShaderLoader;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;
import com.ktar5.slime.screens.mainmenu.LevelSelectionScreen;
import com.ktar5.slime.screens.mainmenu.OptionsMenuScreen;

public class PauseWithBlur extends GameState {
    Stage stage;
    StaticCamera camera = new StaticCamera(new OrthographicCamera(480, 270));
    PostProcessor postProcessor;
    Blur blur;

    public PauseWithBlur(GameScreen gameScreen) {
        super(gameScreen);

        TextureAtlas atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        Skin skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        SpriteBatch batch = new SpriteBatch();

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(camera.getViewport(), batch);


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
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        mainTable.add(levelsButton).pad(10, 0, 0, 0);
        mainTable.row();
        mainTable.add(optionsButton).pad(10, 0, 0, 0);
        mainTable.row();
        mainTable.add(exitButton).pad(10, 0, 0, 0);

        stage.addActor(mainTable);

        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0,
                getGameScreen().getCamera().getViewport().getScreenWidth() * 2,
                getGameScreen().getCamera().getViewport().getScreenHeight() * 2);
        fboRegion.flip(false, true);
        fbo.begin();
        fbo.end();
        stage.addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                //batch.setColor(0, 0, 0, .55f);
                //batch.draw(fboRegion, -50, -230);
                //batch.setColor(1, 1, 1, 1);
            }
        });


        ShaderLoader.BasePath = "shaders/";
        blur = new Blur(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        postProcessor = new PostProcessor(false, false, true);
        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setCenter(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        vignette.setIntensity(1f);
//        Blur blur = new Blur(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        postProcessor.addEffect(vignette);
    }


    @Override
    public void start() {
        EngineManager.get().getConsole().resetInputProcessing();

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage,
                EngineManager.get().getConsole().getInputProcessor(),
                SlimeGame.input);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
//        camera.getCamera().viewportHeight = height;
//        camera.getCamera().viewportWidth = width;
//        camera.getCamera().updateTiles();
//
//        camera.getViewport().setScreenWidth(Gdx.graphics.getWidth());
//        camera.getViewport().setScreenWidth(Gdx.graphics.getHeight());
//        camera.getViewport().apply();

//        stage.getViewport().updateTiles(width, height, true);
        getGameScreen().getCamera().getViewport().update(width, height);
        getGameScreen().getCamera().update(0f);
    }

    @Override
    public void onUpdate(float dTime) {
        stage.act();

        if (KInput.isKeyJustPressed(Input.Keys.ESCAPE)) {
            changeState(Running.class);
        }
    }

    @Override
    protected void end() {

    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
        postProcessor.capture();
        Gdx.gl.glClearColor(168 / 255f, 118 / 255f, 86 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        SlimeGame.getGame().getLevelHandler().render(batch, EngConst.STEP_TIME);
        getGameScreen().getFrameRate().render(batch, EngConst.STEP_TIME);
        getGameScreen().getVersionInfo().render(batch, EngConst.STEP_TIME);

        blur.setAmount(1f);
        blur.setType(Blur.BlurType.Gaussian3x3b);
        blur.setPasses(10);
        blur.render(postProcessor.getCombinedBuffer());
        postProcessor.render();

        stage.draw();
    }
}
