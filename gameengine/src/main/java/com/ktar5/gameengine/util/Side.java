package com.ktar5.gameengine.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Side {
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);

    public static final Side[][] ORDER = {
            {null, DOWN, null},
            {LEFT, null, RIGHT},
            {null, UP, null}
    };
    public final int x, y;

    public static Side of(int x, int y) {
        return ORDER[y + 1][x + 1];
    }

    public static Side ofOpposite(int x, int y) {
        return ORDER[-y + 1][-x + 1];
    }

    public Side opposite() {
        return Side.ofOpposite(this.x, this.y);
    }

    public Side rotateClockwise(int times) {
        return Side.values()[(this.ordinal() + times) % 4];
    }
}
