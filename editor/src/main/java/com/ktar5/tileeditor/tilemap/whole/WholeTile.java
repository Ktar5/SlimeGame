package com.ktar5.tileeditor.tilemap.whole;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.tileeditor.tileset.BaseTileset;
import com.ktar5.tileeditor.tileset.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class WholeTile extends Tile<BaseTileset> {
    private int blockId;
    private int direction;

    public WholeTile(int blockId, int direction, BaseTileset tileset) {
        super(tileset);
        this.blockId = blockId;
        this.direction = direction;
    }

    @Override
    public String serialize() {
        return toString();
    }

    @Override
    public String toString() {
        if (direction == 0) {
            return String.valueOf(blockId);
        } else {
            return blockId + "_" + direction;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WholeTile wholeTile = (WholeTile) o;
        return blockId == wholeTile.blockId &&
                direction == wholeTile.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockId, direction);
    }

    public TextureRegion getTextureRegion() {
        return getTileset().getTileImages().get(blockId);
    }

}
