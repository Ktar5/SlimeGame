package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.EngConst;
import com.ktar5.slime.engine.camera.StaticCamera;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.postprocessing.PostProcessor;
import com.ktar5.slime.engine.postprocessing.effects.Vignette;
import com.ktar5.slime.engine.postprocessing.filters.Blur;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.screens.GameScreen;

import java.util.Collections;
import java.util.List;

public class PauseWithBlur2 extends GameState {
    Stage stage;
    StaticCamera camera = new StaticCamera(new OrthographicCamera(480, 270));
    PostProcessor postProcessor;
    Blur blur;

    public PauseWithBlur2(GameScreen gameScreen) {
        super(gameScreen);

        TextureAtlas atlas = new TextureAtlas("textures/skins/pixel/skin.atlas");
        Skin skin = new Skin(Gdx.files.internal("textures/skins/pixel/skin.json"), atlas);

        SpriteBatch batch = new SpriteBatch();

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(camera.getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        EngineManager.get().getConsole().resetInputProcessing();

        Table levels = new Table();
        levels.top().right();
        levels.pad(15, 0, 0, 15);
        levels.setFillParent(true);
        int levelCount = SlimeGame.getGame().getLevelHandler().getLevelCount();
        for (int i = 0; i < levelCount; i++) {
            TextButton button = new TextButton(String.valueOf(i), skin);
            if (i % 3 == 0) {
                levels.row();
            }
            levels.add(button).pad(0, 0, 5, 5);
            int finalI = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    changeState(Running.class);
                    SlimeGame.getGame().getLevelHandler().setLevel(finalI);
                }
            });
        }


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
        stage.addActor(levels);


        com.bitfire.utils.ShaderLoader.BasePath = "shaders/";
        blur = new Blur(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        postProcessor = new PostProcessor(false, false, true);
        Vignette vignette = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        vignette.setCenter(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        vignette.setIntensity(1f);
//        Blur blur = new Blur(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        postProcessor.addEffect(vignette);
        objects = Collections.singletonList((internalBatch, dTime) -> {

            postProcessor.capture();
            Gdx.gl.glClearColor(131 / 255f, 87 / 255f, 64 / 255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            SlimeGame.getGame().getLevelHandler().render(internalBatch, EngConst.STEP_TIME);
            getGameScreen().getFrameRate().render(internalBatch, EngConst.STEP_TIME);
            getGameScreen().getVersionInfo().render(internalBatch, EngConst.STEP_TIME);

            blur.setAmount(1f);
            blur.setType(Blur.BlurType.Gaussian3x3b);
            blur.setPasses(10);
            blur.render(postProcessor.getCombinedBuffer());
            postProcessor.render();

            stage.act();
            stage.draw();
        });
    }

    List<Renderable> objects;


    @Override
    public void start() {
        getGameScreen().getRenderManager().setRenderables(objects);
    }

    @Override
    public void resize(int width, int height) {
        camera.getCamera().viewportHeight = height;
        camera.getCamera().viewportWidth = width;
        camera.getCamera().update();

//        camera.getViewport().setScreenWidth(Gdx.graphics.getWidth());
//        camera.getViewport().setScreenWidth(Gdx.graphics.getHeight());
//        camera.getViewport().apply();

        stage.getViewport().update(width, height, true);

    }

    @Override
    public void onUpdate(float dTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            changeState(Running.class);
        }
    }

    @Override
    protected void end() {

    }

}
