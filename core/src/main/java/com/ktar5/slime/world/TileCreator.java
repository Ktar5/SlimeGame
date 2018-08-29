package com.ktar5.slime.world;

import com.ktar5.slime.world.grid.TileType;
import com.ktar5.slime.world.grid.tiles.Tile;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.Gate;
import com.ktar5.slime.world.tiles.Spikes;
import com.ktar5.slime.world.tiles.Wall;

public final class TileCreator {
    
    
    public static Tile getTile(int x, int y, int value) {
        if (TileType.HARMFUL.hasId(value)) {
            return new Spikes(x, y);
        } else if (TileType.WALL.hasId(value)) {
            return new Wall(x, y);
        } else if (TileType.AIR.hasId(value)) {
            return new Air(x, y);
        } else if (TileType.WIN.hasId(value)) {
            return new Gate(x, y);
        } else {
            throw new RuntimeException("You fucked up at " + x + ", " + y + " with an id of " + value);
        }
    }
    
}
