package com.ktar5.tileeditor.tilemap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ktar5.tileeditor.scene.utils.ZoomablePannableWidget;

public class TilemapActor extends ZoomablePannableWidget {
    private Tilemap tilemap;

    public TilemapActor(Tilemap tilemap) {
//        this.debug();
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
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin();

        //Down to up
        for (int i = 1; i < tilemap.getNumTilesWide(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getTotalX() + (i * tilemap.getTileWidth() * scale),
                    getTotalY(),
                    getTotalX() + (i * tilemap.getTileWidth() * scale),
                    getTotalY() + (tilemap.getDimensionY() * scale));
        }

        //Left to right
        for (int i = 1; i < tilemap.getNumTilesHigh(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getTotalX(),
                    getTotalY() + (i * tilemap.getTileHeight() * scale),
                    getTotalX() + (tilemap.getDimensionX() * scale),
                    getTotalY() + (i * tilemap.getTileHeight() * scale));
        }

//        shapeRenderer.circle(getContentCenterX(), getContentCenterY(), 5);
        shapeRenderer.end();
        ScissorStack.popScissors();
        batch.begin();
    }


    @Override
    public float getContentCenterX() {
        return getTotalX() + (tilemap.getDimensionX() * scale / 2f);
    }

    @Override
    public float getContentCenterY() {
        return getTotalY() + (tilemap.getDimensionY() * scale / 2f);
    }

}

