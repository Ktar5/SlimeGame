package com.ktar5.tileeditor.tilemap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ktar5.tileeditor.scene.utils.ZoomablePannableWidget;

public class TilemapActor extends ZoomablePannableWidget {
    private Tilemap tilemap;

    public TilemapActor(Tilemap tilemap) {
        this.debug();
        this.tilemap = tilemap;

    }

    Rectangle clip = new Rectangle();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!ScissorStack.pushScissors(clip.set(getX(), getY(), getWidth(), getHeight()))) {
            return;
        }

        tilemap.getLayers().render(batch, this);

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin();

        for (int i = 1; i < tilemap.getNumTilesWide(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    panX + getX() + (getWidth() / 2) + (i * tilemap.getTileWidth() * scale),
                    getY() + panY + (getHeight() / 2) + (tilemap.getTileHeight() * scale) - (tilemap.getTileHeight() * scale),
                    (i * tilemap.getTileWidth() * scale) + panX + getX() + (getWidth() / 2),
                    (tilemap.getDimensionY() * scale) + getY() + panY + (getHeight() / 2));
        }

        for (int i = 1; i < tilemap.getNumTilesHigh(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getX() + panX + (getWidth() / 2),
                    (i * tilemap.getTileHeight() * scale) + panY + getY() + (getHeight() / 2),
                    (tilemap.getDimensionX() * scale) + getX() + panX + (getWidth() / 2),
                    (i * tilemap.getTileHeight() * scale) + panY + getY() + (getHeight() / 2));
        }

//        shapeRenderer.circle(getContentCenterX(), getContentCenterY(), 5);
        shapeRenderer.end();
        ScissorStack.popScissors();
        batch.begin();
    }

    @Override
    public float getContentCenterX() {
        return panX + getX() + (getWidth() / 2f) + (tilemap.getDimensionX() * scale / 2f);
    }

    @Override
    public float getContentCenterY() {
        return panY + getY() + (getHeight() / 2) + (tilemap.getDimensionY() * scale / 2);
    }

}

