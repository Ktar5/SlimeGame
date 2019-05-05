package com.ktar5.tileeditor.tilemap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.tabs.TilemapTab;
import com.ktar5.tileeditor.scene.utils.ZoomablePannableWidget;
import com.ktar5.tileeditor.tilemap.layers.BaseLayer;
import com.ktar5.tileeditor.tilemap.layers.TileLayer;
import com.ktar5.tileeditor.tileset.Tile;

public class TilemapActor extends ZoomablePannableWidget {
    private final Tilemap tilemap;
    private final Rectangle clip = new Rectangle();
    private final Vector2 tileHovered = new Vector2(-1, -1);

    public TilemapActor(Tilemap tilemap) {
        this.tilemap = tilemap;
        addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                tileHovered.x = (getX() + x - getRenderX()) / (scale * tilemap.getTileWidth());
                //This is because (int) -0.5 = 0
                if (tileHovered.x < 0) {
                    tileHovered.x = -2;
                }
                tileHovered.x = (int) tileHovered.x;

                tileHovered.y = (getY() + y - getRenderY()) / (scale * tilemap.getTileHeight());
                //This is because (int) -0.5 = 0
                if (tileHovered.y < 0) {
                    tileHovered.y = -2;
                }
                tileHovered.y = tilemap.getNumTilesHigh() - ((int) tileHovered.y);
                tileHovered.y -= 1;

                if (tileHovered.x >= tilemap.getNumTilesWide() || tileHovered.x < 0
                        || tileHovered.y < 0 || tileHovered.y >= tilemap.getNumTilesHigh()) {
                    tileHovered.x = -1;
                    tileHovered.y = -1;
                }
                tileHovered.y += 1; //compensating for the -= 1 above, idk man, just let it be.
                /*
                 * Note that I could also make it so that, in the above Y checks, the tileHovered.y value
                 * could've been incremented by one, or decremented, idk, I'm really tired. Just let it be
                 * how it wants to be. Mkaaaay? Mkkkayyyy? Thanks. Don't touch it.
                 */

//                Logger.debug("TILE # = " + tileHovered.x + ", " + tileHovered.y);
                return true;
            }

            int buttonTouchedDown = 0;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    Tile selectedTile = getTab().getTilesetSidebar().getSelectedTile();
                    if (selectedTile == null) {
                        return false;
                    }
                    BaseLayer activeLayer = tilemap.getLayers().getActiveLayer();
                    if (activeLayer instanceof TileLayer) {
                        TileLayer tileLayer = (TileLayer) activeLayer;
                        tileLayer.setTile(selectedTile, (int) tileHovered.x, (int) (tilemap.getNumTilesHigh() - tileHovered.y));
                    }
                    getTab().setDirty(true);
                } else if (button == Input.Buttons.RIGHT) {
                    BaseLayer activeLayer = tilemap.getLayers().getActiveLayer();
                    if (activeLayer instanceof TileLayer) {
                        TileLayer tileLayer = (TileLayer) activeLayer;
                        tileLayer.setTile(null, (int) tileHovered.x, (int) (tilemap.getNumTilesHigh() - tileHovered.y));
                    }
                    getTab().setDirty(true);
                }
                buttonTouchedDown = button;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                mouseMoved(event, x, y);
                touchDown(event, x, y, pointer, buttonTouchedDown);
            }
        });
    }

    public TilemapTab getTab() {
        return (TilemapTab) Main.getInstance().getRoot().getTabHoldingPane().getTab(tilemap.getId());
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!ScissorStack.pushScissors(clip.set(getX(), getY(), getWidth(), getHeight()))) {
            return;
        }
        tilemap.getLayers().render(batch, this);

        //DON'T YOU FUCKING DARE TOUCH THIS CODE ITS FUCKING MAGIC AND IT FUCKING WORKS EAT MY ASS LIBGDX
        if (tileHovered.x != -1) {
            float x = getRenderX() + (tileHovered.x * tilemap.getTileWidth() * scale);
            float y = getRenderY() + (tilemap.getDimensionY() * scale);
            y = y - (tileHovered.y * tilemap.getTileHeight() * scale);
            if (getTab().getTilesetSidebar().getSelectedTile() != null) {
                batch.draw(getTab().getTilesetSidebar().getSelectedTile().getTextureRegion(), (int) x, (int) y, 0, 0,
                        tilemap.getTileWidth(), tilemap.getTileHeight(), scale, scale, 0);
            }
            batch.draw(Main.getInstance().getSelection(), (int) x, (int) y, 0, 0,
                    tilemap.getTileWidth(), tilemap.getTileHeight(), scale, scale, 0);
        }
        //END OF CODE THAT YOU SHOULDN'T FUCKING TOUCH I'LL KILL YOUR FAMILY IF YOU TOUCH THIS

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin();

//        shapeRenderer.setColor(Color.BLUE);
//        shapeRenderer.circle(getRenderX(), getRenderY(), 5);
//        shapeRenderer.setColor(Color.GREEN);
//        shapeRenderer.circle(getContentCenterX(), getContentCenterY(), 5);
//        shapeRenderer.setColor(Color.WHITE);
        //Down to up
        for (int i = 1; i < tilemap.getNumTilesWide(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getRenderX() + (i * tilemap.getTileWidth() * scale),
                    getRenderY(),
                    getRenderX() + (i * tilemap.getTileWidth() * scale),
                    getRenderY() + (tilemap.getDimensionY() * scale));
        }

        //Left to right
        for (int i = 1; i < tilemap.getNumTilesHigh(); i++) {
            drawDottedLine(shapeRenderer, 2,
                    getRenderX(),
                    getRenderY() + (i * tilemap.getTileHeight() * scale),
                    getRenderX() + (tilemap.getDimensionX() * scale),
                    getRenderY() + (i * tilemap.getTileHeight() * scale));
        }

//        shapeRenderer.circle(getContentCenterX(), getContentCenterY(), 5);
        shapeRenderer.end();
        ScissorStack.popScissors();
        batch.begin();
    }


    @Override
    public float getContentCenterX() {
        return getRenderX() + (tilemap.getDimensionX() * scale / 2f);
    }

    @Override
    public float getContentCenterY() {
        return getRenderY() + (tilemap.getDimensionY() * scale / 2f);
    }

}

