package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.ktar5.gameengine.camera.StaticCamera;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;

import java.util.Arrays;

public class Pause extends GameState {
    Stage stage;
    StaticCamera camera = new StaticCamera(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));


    public Pause(GameScreen gameScreen) {
        super(gameScreen);

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
                batch.setColor(0, 0, 0, .55f);
                batch.draw(fboRegion, -50, -230);
                batch.setColor(1, 1, 1, 1);
            }
        });
        stage.addActor(levels);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void start() {
        getGameScreen().getRenderManager().setRenderables(Arrays.asList(
                SlimeGame.getGame().getLevelHandler(),
                getGameScreen().getFrameRate(),
                getGameScreen().getVersionInfo(),
                (batch, dTime) -> {
                    stage.act();
                    stage.draw();
                }
        ));
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
