package com.ktar5.slime.world.level.types;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.IntMap;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.tiles.*;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.Tile;

public enum TileType {
    WIN((x, y, cell) -> new Win(x, y), 0),
    WALL((x, y, cell) -> new Wall(x, y), 2),
    ONE_DIRECTION((x, y, cell) -> new OneDirection(x, y, Rotation.fromCell(cell)), 3),
    DRAIN((x, y, cell) -> new Drain(x, y), 4),
    GOO((x, y, cell) -> new Goo(x, y), 5),
    BUTTON((x, y, cell) -> new Button(x, y, Rotation.fromCell(cell)), 8),
    PRESSURE_PLATE((x, y, cell) -> new PressurePlate(x, y), 9),
    BOX(null, 10),
    GATE((x, y, cell) -> new Gate(x, y, Rotation.fromCell(cell)), 11),
    DRAIN_PIPE_LEFT_UP((x, y, cell) -> new HoleInWall(x, y, Rotation.fromCell(cell), Side.LEFT, Side.UP), 12),
    DRAIN_PIPE_UP_DOWN((x, y, cell) -> new HoleInWall(x, y, Rotation.fromCell(cell), Side.UP, Side.DOWN), 20),
    DRAIN_PIPE_ALL_DIR((x, y, cell) -> new HoleInWall(x, y), 28),

    SPIKE((x, y, cell) -> new Spikes(x, y), 16),
    HOLE((x, y, cell) -> new Hole(x, y), 17),
    CRUMBLING_FLOOR((x, y, cell) -> new CrumbledFloor(x, y), 18),
    TELEPORTER((x, y, cell) -> new Teleporter(x, y), 19),

    GHOST(null, 24),
    STOMPER(null, 25),
    FLAMETHROWER(null, 26),
    BIG((x, y, cell) -> new Big(x, y), 27);
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
