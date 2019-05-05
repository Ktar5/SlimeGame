package com.ktar5.tileeditor.scene.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.ktar5.tileeditor.Main;
import lombok.Getter;

@Getter
public abstract class ZoomablePannableWidget extends Widget {
    public static final double[] ZOOM_LEVELS = {
            0.015625, 0.03125, 0.0625, 0.125, 0.25, 0.33, 0.5,
            0.75, 1.0, 1.5, 2.0, 3.0, 4.0, 5.5, 8.0, 11.0, 16.0,
            23.0, 32.0, 45.0, 64.0, 90.0, 128.0, 180.0, 256.0
    };
    protected ShapeRenderer shapeRenderer;

    protected int zoomLevel = 8;
    protected float scale = 1;
    protected float panX, panY;

    public ZoomablePannableWidget() {
//        debug();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(Main.getInstance().getRoot().getCamera().combined);
        shapeRenderer.setAutoShapeType(true);

        addListener(new InputListener() {
            private float lastX, lastY;
            private boolean isDragging = false;

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.MIDDLE) {
                    isDragging = false;
                }
            }

            //TODO have a max allowed distance away from the edge from the center
            @Override
            public void touchDragged(InputEvent event, float mx, float my, int pointer) {
                if (!isDragging) {
                    return;
                }

                panX += mx - lastX;
                panY += my - lastY;

                float leftX = getRenderX();
                float bottomY = getRenderY();

                if (leftX >= getWidth() + getX() + 10 * scale|| leftX + ((getContentCenterX() - leftX) * 2) < 10 * scale) {
                    panX -= mx - lastX;
                }
                if (bottomY >= getHeight() + getY() + 10 * scale || bottomY + ((getContentCenterY() - bottomY) * 2) < 10 * scale) {
                    panY -= my - lastY;
                }
//                if (getContentCenterX() >= getWidth() + getX() || getContentCenterX() <= 0 + getX()) {
//                    panX -= mx - lastX;
//                }
//                if (getContentCenterY() >= getHeight() || getContentCenterY() <= 0) {
//                    panY -= my - lastY;
//                }
                lastX = mx;
                lastY = my;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.MIDDLE) {
                    isDragging = true;
                    lastX = x;
                    lastY = y;
                    return true;
                } else {
                    return false;
                }
            }

        });
    }

    protected void drawDottedLine(ShapeRenderer shapeRenderer, int dotDist, float x1, float y1, float x2, float y2) {
        Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
        float length = vec2.len();
        for (int i = 0; i < length; i += dotDist) {
            vec2.clamp(length - i, length - i);
            shapeRenderer.point((int) (x1 + vec2.x), (int) (y1 + vec2.y), 0);
        }
    }

    public void scale(int amount) {
        float previousCX = getContentCenterX();
        float previousCY = getContentCenterY();

        zoomLevel += amount < 0 ? 1 : -1;

        if (zoomLevel >= ZOOM_LEVELS.length) {
            zoomLevel -= 1;
        } else if (zoomLevel <= 0) {
            zoomLevel = 0;
        }
        scale = ((float) ZOOM_LEVELS[zoomLevel]);

        float newCX = getContentCenterX();
        float newCY = getContentCenterY();

        panX = panX - (newCX - previousCX);
        panY = panY - (newCY - previousCY);
    }

    public float getRenderX() {
        return getPanX() + getX() + (getWidth() / 2);
    }

    public float getRenderY() {
        return getPanY() + getY() + (getHeight() / 2);
    }

    public abstract float getContentCenterX();

    public abstract float getContentCenterY();
}
