package com.ktar5.tileeditor.tileset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.tileeditor.util.ToolSerializeable;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Tile implements ToolSerializeable {
    private Tileset tileset;
    private int blockId;
    private int direction;

    public Tile(int blockId, int direction, Tileset tileset) {
        this.tileset = tileset;
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
        Tile tile = (Tile) o;
        return blockId == tile.blockId &&
                direction == tile.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockId, direction);
    }

    public Pair toXY() {
        int y = getBlockId() / tileset.getColumns();
        int x = getBlockId() % tileset.getColumns();
        return new Pair(x, y);
    }

    public TextureRegion getTextureRegion() {
        return getTileset().getTileImages().get(blockId);
    }

}

