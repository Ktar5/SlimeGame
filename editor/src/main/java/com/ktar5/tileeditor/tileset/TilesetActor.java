package com.ktar5.tileeditor.tileset;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ktar5.tileeditor.scene.utils.ZoomablePannableWidget;

public class TilesetActor extends ZoomablePannableWidget {
    private final boolean handleClipping;
    private final BaseTileset tileset;

    public TilesetActor(BaseTileset tileset, boolean handleClipping) {
//        this.debug();
        this.handleClipping = handleClipping;
        this.tileset = tileset;
    }

    Rectangle clip = new Rectangle();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        if (handleClipping) {
            batch.flush();
            if (!ScissorStack.pushScissors(clip.set(getX(), getY(), getWidth(), getHeight()))) {
                return;
            }
        }
        for (int i = 0; i < tileset.getTileImages().size; i++) {
            int row = tileset.getColumns() - (i / tileset.getColumns());
            int col = i % tileset.getColumns();
            TextureRegion region = tileset.getTileImages().get(i);
            float x = panX + getX() + (getWidth() / 2) + (col * tileset.getTileWidth() * scale);
            float y = panY + getY() + (getHeight() / 2) + (row * tileset.getTileHeight() * scale);
            //todo see if i should remove the int positions
            batch.draw(region, (int) x, (int) y, 0, 0, tileset.getTileWidth(), tileset.getTileHeight(),
                    scale, scale, 0);
        }

        batch.end();

        shapeRenderer.begin();

        for (int i = 1; i < tileset.getColumns(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    panX + getX() + (getWidth() / 2) + (i * tileset.getTileWidth() * scale),
                    getY() + panY + (getHeight() / 2) + (tileset.getTileHeight() * scale),
                    panX + getX() + (getWidth() / 2) + (i * tileset.getTileWidth() * scale),
                    getY() + panY + (getHeight() / 2) + (tileset.getTileHeight() * scale) + (tileset.getDimensionY() * scale));
        }

        for (int i = 1; i < tileset.getRows(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getX() + panX + (getWidth() / 2),
                    (i * tileset.getTileHeight() * scale) + panY + getY() + (getHeight() / 2) + (tileset.getTileHeight() * scale),
                    (tileset.getDimensionX() * scale) + getX() + panX + (getWidth() / 2),
                    (i * tileset.getTileHeight() * scale) + panY + getY() + (getHeight() / 2) + (tileset.getTileHeight() * scale));
        }

        shapeRenderer.end();
        if (handleClipping) {
            ScissorStack.popScissors();
        }
        batch.begin();
    }

    @Override
    public float getContentCenterX() {
        return panX + getX() + (getWidth() / 2f) + (tileset.getDimensionX() * scale / 2f);
    }

    @Override
    public float getContentCenterY() {
        return panY + getY() + (getHeight() / 2) + (tileset.getDimensionY() * scale / 2) + (tileset.getTileHeight() * scale);
    }
}
