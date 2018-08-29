package com.ktar5.slime.world.grid;

import com.badlogic.gdx.utils.IntMap;

public enum TileType {
    AIR(-1),
    WALL(1256),
    HARMFUL(385),
    WIN(343),
    MISC(-2);
    
    private static final IntMap<TileType> tileIds = new IntMap<>();
    
    static {
        for (TileType t : TileType.values()) {
            for (int id : t.ids) {
                tileIds.put(id, t);
            }
        }
    }
    
    public static TileType tileFromId(int id) {
        return tileIds.get(id);
    }
    
    
    private int[] ids;
    
    TileType(int... ids) {
        this.ids = ids;
    }
    
    public boolean hasId(int id) {
        for (int value : ids) {
            if (value == id) return true;
        }
        return false;
    }
    
}
