package com.ktar5.slime.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Side {
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);
    
    public final int x, y;
    public static final Side[][] ORDER = {
            {null, DOWN, null},
            {LEFT, null, RIGHT},
            {null, UP, null}
    };
    
    public static Side of(int x, int y) {
        return ORDER[y + 1][x + 1];
    }
    
    public static Side ofOpposite(int x, int y) {
        return ORDER[-y + 1][-x + 1];
    }
    
    public Side opposite() {
        return Side.ofOpposite(this.x, this.y);
    }
}
