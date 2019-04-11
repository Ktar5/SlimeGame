package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.camera.StaticCamera;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;

import java.util.Collections;

public class PauseWithBlur extends GameState {
    private Stage stage;
    private StaticCamera camera = new StaticCamera(new OrthographicCamera(480, 270));

    private ShaderProgram shader;
    private FrameBuffer fboA;
    private FrameBuffer fboB;

    public PauseWithBlur(GameScreen gameScreen) {
        super(gameScreen);
        shader = new ShaderProgram(Gdx.files.internal("shaders/blur.vert"), Gdx.files.internal("shaders/blur.frag"));
        fboA = new FrameBuffer(Pixmap.Format.RGBA8888, 960, 540, false);
        fboB = new FrameBuffer(Pixmap.Format.RGBA8888, 960, 540, false);

        shader.begin();
        shader.setUniformf("resolution", 960);
        shader.end();

        getGameScreen().getRenderManager().setRenderables(Collections.singletonList((internalBatch, dTime) -> {
            internalBatch.begin();

            fboA.begin();
            internalBatch.setShader(null);

            SlimeGame.getGame().getLevelHandler().render(internalBatch, EngConst.STEP_TIME);
            getGameScreen().getFrameRate().render(internalBatch, EngConst.STEP_TIME);
            getGameScreen().getVersionInfo().render(internalBatch, EngConst.STEP_TIME);

            internalBatch.flush();
            fboA.end();
            applyBlur(1f, internalBatch);

            internalBatch.end();

            stage.act();
            stage.draw();
        }));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void start() {
    }

    public void renderPreBlur(SpriteBatch batch) {
        SlimeGame.getGame().getLevelHandler().render(batch, EngConst.STEP_TIME);
        getGameScreen().getFrameRate().render(batch, EngConst.STEP_TIME);
        getGameScreen().getVersionInfo().render(batch, EngConst.STEP_TIME);
    }

    private void drawTexture(Texture texture, float x, float y, Batch batch) {
        int width = texture.getWidth();
        int height = texture.getHeight();

        batch.draw(texture,
                x, y,
                0.0f, 0.0f,
                width, height,
                1f, 1f,
                0.0f,
                0, 0,
                width, height,
                false, false);
    }

    private void applyBlur(float blur, Batch batch) {
        // Horizontal blur from FBO A to FBO B
        fboB.begin();
        batch.setShader(shader);
        shader.setUniformf("dir", 1.0f, 0.0f);
        shader.setUniformf("radius", blur);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        drawTexture(fboA.getColorBufferTexture(), 0.0f, 0.0f, batch);
        batch.flush();
        fboB.end();

        // Vertical blur from FBO B to the screen
        shader.setUniformf("dir", 0.0f, 1.0f);
        shader.setUniformf("radius", blur);
        drawTexture(fboB.getColorBufferTexture(), 0.0f, 0.0f, batch);
        batch.flush();
    }

    @Override
    public void onUpdate(float dTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            changeState(Running.class);
        }
    }

    @Override
    protected void end() {
/*
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0,
                getGameScreen().getCamera().getViewport().getScreenWidth() * 2,
                getGameScreen().getCamera().getViewport().getScreenHeight() * 2);
        fboRegion.flip(false, true);
        fbo.begin();
        fbo.end();
        TextureAtlas atlas = new TextureAtlas("default_skin/uiskin.atlas");
        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"), atlas);

        SpriteBatch batch = new SpriteBatch();

        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
        camera.getCamera().update();

        stage = new Stage(camera.getViewport(), batch);

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
                    SlimeGame.getGame().setScreen(new GameScreen());
                    SlimeGame.getGame().getLevelHandler().setLevel(finalI);
                }
            });
        }
        stage.addActor(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.setColor(0, 0, 0, .55f);
                batch.draw(fboRegion, -50, -230);
                batch.setColor(1, 1, 1, 1);
            }
        });
        stage.addActor(levels);

 */
    }

}
