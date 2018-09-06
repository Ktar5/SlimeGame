package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.IntMap;
import com.ktar5.slime.world.tiles.*;

public enum TileType {
    WIN((x, y, cell) -> new Win(x, y), 0),
    GATE((x, y, cell) -> { return new Gate(x, y, Rotation.fromCell(cell)); }, 2, 3),
    WALL((x, y, cell) -> new Wall(x, y), 32),
    BUTTON((x, y, cell) -> {
        System.out.println("X: " + x + " Y: " + y);
        return new Button(x, y, Rotation.fromCell(cell));
    }, 33),
    PRESSURE_PLATE((x, y, cell) -> new PressurePlate(x, y), 34),
    BOX(null, 35),
    SPIKE((x, y, cell) -> new Spikes(x, y), 64),
    HOLE((x, y, cell) -> new Hole(x, y), 65),
    CRUMBLING_FLOOR((x, y, cell) -> new CrumbledFloor(x, y), 66),
    FLAMETHROWER(null, 67),
    GHOST(null, 96),
    STOMPER(null, 97),
    DRAIN((x, y, cell) -> new Drain(x, y), 98),
    DRAIN_PIPE(null, 99);

    private static final IntMap<TileType> tileIds = new IntMap<>();

    static {
        for (TileType t : TileType.values()) {
            for (int id : t.ids) {
                tileIds.put(id, t);
            }
        }
    }

    public final TileGenerator generator;
    private final int[] ids;
    TileType(TileGenerator generator, int... ids) {
        this.ids = ids;
        this.generator = generator;
    }

    public static TileType tileFromId(int id) {
        return tileIds.get(id);
    }

    public boolean hasId(int id) {
        for (int value : ids) {
            if (value == id) return true;
        }
        return false;
    }

    public static interface TileGenerator {
        public Tile getTile(int x, int y, TiledMapTileLayer.Cell cell);
    }

}
