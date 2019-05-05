package com.ktar5.tileeditor.tileset;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ktar5.tileeditor.scene.utils.ZoomablePannableWidget;
import lombok.Getter;

@Getter
public class TilesetActor extends ZoomablePannableWidget {
    private final Tileset tileset;

    public TilesetActor(Tileset tileset) {
        this.tileset = tileset;
    }

    private Rectangle clip = new Rectangle();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        batch.flush();

        Vector3 translation = batch.getTransformMatrix().getTranslation(new Vector3(getX(), getY(), 0));
        if (!ScissorStack.pushScissors(clip.set(translation.x, translation.y, getWidth(), getHeight()))) {
            return;
        }

        //Render tiles
        for (int i = 0; i < tileset.getTileImages().size; i++) {
            int row = (tileset.getRows() - (i / tileset.getColumns())) - 1;
            int col = i % tileset.getColumns();

            float x = getRenderX() + (col * tileset.getTileWidth() * scale);
            float y = getRenderY() + (row * tileset.getTileHeight() * scale);

            batch.draw(tileset.getTileImages().get(i), (int) x, (int) y, 0, 0,
                    tileset.getTileWidth(), tileset.getTileHeight(), scale, scale, 0);
        }

        batch.end();

        shapeRenderer.begin();

        shapeRenderer.circle(getRenderX(), getRenderY(), 5);

        //Down to up
        for (int i = 1; i < tileset.getColumns(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getRenderX() + (i * tileset.getTileWidth() * scale),
                    getRenderY(),
                    getRenderX() + (i * tileset.getTileWidth() * scale),
                    getRenderY() + (tileset.getDimensionY() * scale));
        }

        //Left to right
        for (int i = 1; i < tileset.getRows(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getRenderX(),
                    getRenderY() + (i * tileset.getTileHeight() * scale),
                    getRenderX() + (tileset.getDimensionX() * scale),
                    getRenderY() + (i * tileset.getTileHeight() * scale));
        }

        shapeRenderer.end();
        ScissorStack.popScissors();
        batch.begin();
    }

    @Override
    public float getContentCenterX() {
        return getRenderX() + (tileset.getDimensionX() * scale / 2f);
    }

    @Override
    public float getContentCenterY() {
        return getRenderY() + (tileset.getDimensionY() * scale / 2f);
    }
}
