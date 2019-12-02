package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.ktar5.gameengine.input.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.GameScreen;

public class Pause extends GameState {
    Stage stage;

    public Pause(GameScreen gameScreen) {
        super(gameScreen);

        TextureAtlas atlas = new TextureAtlas("default_skin/uiskin.atlas");
        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"), atlas);

//        camera.getCamera().position.set(camera.getCamera().viewportWidth / 2, camera.getCamera().viewportHeight / 2, 0);
//        camera.getCamera().update();

        stage = new Stage(SlimeGame.getGame().getUiCamera().getViewport(), SlimeGame.getGame().getSpriteBatch());

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
            levels.add(button).pad(0, 0, 5, 5).width(25f);
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
        getGameScreen().getCamera().getViewport().update(width, height);
        getGameScreen().getCamera().getCamera().position.set(getGameScreen().getCamera().getCamera().viewportWidth / 2, getGameScreen().getCamera().getCamera().viewportHeight / 2, 0);
        getGameScreen().getCamera().getCamera().update();
    }

    @Override
    public void start() {

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
        SlimeGame.getGame().getLevelHandler().render(batch, dTime);
//        getGameScreen().getFrameRate().render(batch, dTime);
//        getGameScreen().getVersionInfo().render(batch, dTime);
        stage.draw();
    }


}
