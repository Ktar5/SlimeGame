package com.ktar5.gameengine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;

public interface Renderable {

    public void render(SpriteBatch batch, float dTime);

    public default void debug(float dTime) {

    }

    static ShapeRenderer getShapeRenderer() {
        return EngineManager.get().getGame().getShapeRenderer();
    }

    static SpriteBatch getSpriteBatch() {
        return EngineManager.get().getGame().getSpriteBatch();
    }

    public static void renderScale(TextureRegion region, float x, float y, float unitsX, float unitsY, float angle) {
        getSpriteBatch().draw(region,
                x - (region.getRegionWidth() / (2)),
                y - (region.getRegionHeight() / (2)),
                region.getRegionWidth() / 2, region.getRegionHeight() / 2,
                region.getRegionWidth(), region.getRegionHeight(),
                unitsX / region.getRegionWidth(),
                unitsY / region.getRegionHeight(),
                angle);
    }

    public static void drawLine(Vector2 start, Vector2 end, int lineWidth, Color color) {
        Gdx.gl.glLineWidth(lineWidth);
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        getShapeRenderer().setColor(color);
        getShapeRenderer().line(start, end);
        getShapeRenderer().end();
        Gdx.gl.glLineWidth(1);
    }

    public static void drawSquare(Vector2 point, float width, float height, Color color) {
        drawSquare(point.x, point.y, width, height, color);
    }

    public static void drawSquare(float posX, float posY, float width, float height, Color color) {
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getShapeRenderer().setColor(color);
        getShapeRenderer().rect(posX, posY, width, height);
        getShapeRenderer().end();
        Gdx.gl.glLineWidth(1);
    }

}
