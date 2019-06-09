package com.ktar5.gameengine.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Side {
    UP(0, 1, "u"),
    RIGHT(1, 0, "r"),
    DOWN(0, -1, "d"),
    LEFT(-1, 0, "l");

    public static final Side[][] ORDER = {
            {null, DOWN, null},
            {LEFT, null, RIGHT},
            {null, UP, null}
    };

    public static final Side[] CCORDER = {DOWN, RIGHT, UP, LEFT};

    public final int x, y;
    public final String string;

    public int ofCCOrder(){
        switch (this){
            case UP: return 2;
            case RIGHT: return 1;
            case DOWN: return 0;
            case LEFT: return 3;
        }
        return -1;
    }

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
