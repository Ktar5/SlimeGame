package com.ktar5.slime.screens.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.screens.GameScreen;

import java.util.Arrays;

public class Pause extends GameState {
    public Pause(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        TextureRegion fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0,
                getGameScreen().getCamera().getViewport().getScreenWidth() * 2,
                getGameScreen().getCamera().getViewport().getScreenHeight() * 2);
        fboRegion.flip(false, true);
        fbo.begin();
        fbo.end();
        Renderable blur = new Renderable() {
            @Override
            public void render(SpriteBatch batch, float dTime) {
                batch.setColor(0, 0, 0, .55f);
                batch.draw(fboRegion, -50, -230);
                batch.setColor(1, 1, 1, 1);
            }
        };
        getGameScreen().getRenderManager().setRenderables(Arrays.asList(
                SlimeGame.getGame().getLevelHandler(),
                getGameScreen().getFrameRate(),
                getGameScreen().getVersionInfo(),
                blur));
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
