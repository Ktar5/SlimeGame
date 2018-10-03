package com.ktar5.slime.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.Renderable;

public class FrameRate implements Disposable, Updatable, Renderable {
    long lastTimeCounted;
    private float sinceChange;
    private float frameRate;
    private BitmapFont font;

    public FrameRate() {
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        font = new BitmapFont();
        font.getData().setScale(1);
        font.setColor(Color.BLACK);
    }

    public void dispose() {
        font.dispose();
    }

    @Override
    public void render(SpriteBatch batch, float dTime) {
        Vector3 v3 = EngineManager.get().getCameraBase().getCamera().unproject(new Vector3(20, 20 , 0));
        font.draw(batch, (int) frameRate + " fps", v3.x, v3.y);
    }

    @Override
    public void update(float dTime) {
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if (sinceChange >= 1000) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
    }
}