package com.ktar5.tileeditor.tileset;

import com.ktar5.tileeditor.util.ToolSerializeable;
import lombok.Getter;

@Getter
public abstract class Tile<T extends BaseTileset> implements ToolSerializeable {
    private T tileset;

    public Tile(T tileset) {
        this.tileset = tileset;
    }

}

