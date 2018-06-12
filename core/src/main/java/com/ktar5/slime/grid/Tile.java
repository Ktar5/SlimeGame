package com.ktar5.slime.grid;

import lombok.Getter;

@Getter
public class Tile {
    public final int x, y;
    private final String id;
    private TileType type;

    public Tile(int x, int y, TileType tileType) {
        this.id = x + "." + y;
        this.type = tileType;
        this.x = x;
        this.y = y;
    }

}

