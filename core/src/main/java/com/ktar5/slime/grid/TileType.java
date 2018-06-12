package com.ktar5.slime.grid;

import com.badlogic.gdx.utils.IntMap;

public enum TileType {
    AIR(-1),
    WALL(120),
    HARMFUL(34),
    WIN(36);

    private static final IntMap<TileType> tileIds = new IntMap<>();

    static {
        for (TileType t : TileType.values()) {
            for (int id : t.ids) {
                tileIds.put(id, t);
            }
        }
    }


    private int[] ids;

    TileType(int... ids) {
        this.ids = ids;
    }

    public static TileType tileFromId(int id) {
        return tileIds.get(id);
    }
}
