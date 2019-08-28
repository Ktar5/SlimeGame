package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.IntMap;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.box.BoxEntityData;
import com.ktar5.slime.entities.cart.CartEntityData;
import com.ktar5.slime.entities.ghost.GhostEntityData;
import com.ktar5.slime.world.tiles.*;
import com.ktar5.slime.world.tiles.base.Rotation;

import java.util.Arrays;
import java.util.HashSet;

public enum TileObjectTypes {
    WIN((x, y, cell) -> new Win(x, y), 0),
    START((x, y, cell) -> new Air(x, y), 1),
    WALL((x, y, cell) -> new Wall(x, y), 2),
    ONE_DIRECTION((x, y, cell) -> new OneDirection(x, y, Rotation.fromCell(cell)), 3),
    DRAIN((x, y, cell) -> new Drain(x, y), 4),
    GOO((x, y, cell) -> new Goo(x, y), 5),
    BUTTON((x, y, cell) -> new Button(x, y, Rotation.fromCell(cell)), 8),
    PRESSURE_PLATE((x, y, cell) -> new PressurePlate(x, y), 9),
    HEAVY_PLATE((x, y, cell) -> new HeavyPressurePlate(x, y), 25),

    GATE((x, y, cell) -> new Gate(x, y, Rotation.fromCell(cell)), 11),
    DRAIN_PIPE_LEFT_UP((x, y, cell) -> new HoleInWall(x, y, Rotation.fromCell(cell), Side.LEFT, Side.UP), 12),
    DRAIN_PIPE_UP_DOWN((x, y, cell) -> new HoleInWall(x, y, Rotation.fromCell(cell), Side.UP, Side.DOWN), 20),
    DRAIN_PIPE_ALL_DIR((x, y, cell) -> new HoleInWall(x, y), 28),

    RAIL_LEFT_UP((x, y, cell) -> new Rail(x, y, Rotation.fromCell(cell), Side.LEFT, Side.UP), 40),
    RAIL_UP_DOWN((x, y, cell) -> new Rail(x, y, Rotation.fromCell(cell), Side.UP, Side.DOWN), 48),
    RAIL_ALL_DIR((x, y, cell) -> new Rail(x, y), 56),

    SPIKE((x, y, cell) -> new Spikes(x, y), 16),
    HOLE((x, y, cell) -> new Hole(x, y), 17),
    CRUMBLING_FLOOR((x, y, cell) -> new CrumbledFloor(x, y), 18),
    TELEPORTER((x, y, cell) -> new Teleporter(x, y), 19),

    HIDESPIKE((x, y, cell) -> new RetractingSpikes(x, y, Rotation.fromCell(cell)), 13),
    SHOOTER((x, y, cell) -> new Shooter(x, y, Rotation.fromCell(cell)), 6),

    BIG((x, y, cell) -> new Big(x, y), 27),

    //Entities / Special Cases
    BOX((x, y, cell) -> new BoxEntityData(x, y), 10),
    HERO((x, y, cell) -> new GhostEntityData(x, y, Rotation.fromCell(cell)), 24),
    CART((x, y, cell) -> new CartEntityData(x, y, Rotation.fromCell(cell), false), 32),
    CART_MOVE((x, y, cell) -> new CartEntityData(x, y, Rotation.fromCell(cell), true), 33),
    ;

    private static final IntMap<TileObjectTypes> tileIds = new IntMap<>();
    public static final HashSet<TileObjectTypes> TILES = new HashSet<>(Arrays.asList(
            WIN, START, WALL, ONE_DIRECTION, DRAIN, GOO, BUTTON, PRESSURE_PLATE, GATE, DRAIN_PIPE_ALL_DIR,
            DRAIN_PIPE_LEFT_UP, DRAIN_PIPE_UP_DOWN, SPIKE, HOLE, CRUMBLING_FLOOR, TELEPORTER, BIG, HIDESPIKE, SHOOTER,
            HEAVY_PLATE, RAIL_ALL_DIR, RAIL_LEFT_UP, RAIL_UP_DOWN
    ));
    public static final HashSet<TileObjectTypes> ENTITIES = new HashSet<TileObjectTypes>(Arrays.asList(
            BOX, HERO, CART, CART_MOVE
    ));

    static {
        for (TileObjectTypes t : TileObjectTypes.values()) {
            for (int id : t.ids) {
                tileIds.put(id, t);
            }
        }
    }

    public final TileObjectGenerator generator;
    private final int[] ids;

    TileObjectTypes(TileObjectGenerator generator, int... ids) {
        this.ids = ids;
        this.generator = generator;
    }

    public boolean isTile() {
        return TILES.contains(this);
    }

    public boolean isEntity() {
        return ENTITIES.contains(this);
    }

    public static TileObjectTypes tileFromId(int id) {
        return tileIds.get(id);
    }

    public boolean hasId(int id) {
        for (int value : ids) {
            if (value == id) return true;
        }
        return false;
    }

    public interface TileObjectGenerator {
        Object get(int x, int y, TiledMapTileLayer.Cell tile);
    }

}
