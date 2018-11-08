package com.ktar5.slime.engine.util.textoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class FrameRate extends TextDisplay {
    long lastTimeCounted;
    private float sinceChange;
    private float frameRate;

    public FrameRate() {
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
    }

    @Override
    public Vector3 getLocation() {
        return new Vector3(20, 20, 0);
    }

    @Override
    public String getText() {
        return (int) frameRate + " fps";
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