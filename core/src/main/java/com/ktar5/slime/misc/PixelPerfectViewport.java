package com.ktar5.slime.misc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PixelPerfectViewport extends Viewport {
    /**
     * Creates a new viewport using a new {@link OrthographicCamera}.
     */
    public PixelPerfectViewport(float worldWidth, float worldHeight) {
        this(worldWidth, worldHeight, new OrthographicCamera());
    }

    public PixelPerfectViewport(float worldWidth, float worldHeight, Camera camera) {
        setWorldSize(worldWidth, worldHeight);
        setCamera(camera);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        Vector2 scaled = new Vector2();

        float screenRatio = screenHeight / screenWidth;
        float sourceRatio = getWorldHeight() / getWorldWidth();
        float scale = screenRatio > sourceRatio ? screenWidth / getWorldWidth() : screenHeight / getWorldHeight();
        scale = (int) scale;
        System.out.println("Viewport Scale: " + scale);
        scaled.x = getWorldWidth() * scale;
        scaled.y = getWorldHeight() * scale;

        int viewportWidth = Math.round(scaled.x);
        int viewportHeight = Math.round(scaled.y);

        // Center.
        setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);

        apply(centerCamera);
    }

}