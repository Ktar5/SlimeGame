package com.ktar5.tileeditor.tilemap.whole;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.tileeditor.tileset.BaseTileset;
import org.json.JSONObject;

import java.io.File;

public class WholeTileset extends BaseTileset {
    
    public WholeTileset(File tilesetFile, JSONObject json) {
        super(tilesetFile, json);
    }
    
    public WholeTileset(File sourceFile, File saveFile, int paddingVertical, int paddingHorizontal, int offsetLeft, int offsetUp, int tileWidth, int tileHeight) {
        super(sourceFile, saveFile, paddingVertical, paddingHorizontal, offsetLeft, offsetUp, tileWidth, tileHeight);
    }

    @Override
    public void getTilesetImages(Texture texture) {
        int index = 0;

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                TextureRegion region = new TextureRegion(texture,
                        getOffsetLeft() + getPaddingHorizontal() + (((2 * getPaddingHorizontal()) + getTileWidth()) * col),
                        getOffsetUp() + getPaddingVertical() + (((2 * getPaddingVertical()) + getTileHeight()) * row),
                        getTileWidth(), getTileHeight()
                );
                this.getTileImages().put(index++, region);
            }
        }
    }
    
}
