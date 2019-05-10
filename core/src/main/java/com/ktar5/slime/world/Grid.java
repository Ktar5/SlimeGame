package com.ktar5.slime.world;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.gameengine.util.Updatable;
import com.ktar5.slime.world.tiles.base.Tile;

import java.util.ArrayList;
import java.util.List;

public class Grid implements Updatable {
    public final int width, height;
    public Tile[][] grid;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        initialize();
    }

    public void initialize() {
        this.grid = new Tile[width][height];
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
//                    grid[x][y] = new WholeTile(x, y, TileObjectTypes.WALL);
//                } else {
//                    grid[x][y] = new WholeTile(x, y, TileObjectTypes.AIR);
//                }
//            }
//        }
    }

    public Tile tileFromDirection(int x, int y, Side side) {
        x += side.x;
        y += side.y;
        if (isInMapRange(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    private void entityTouchedTileSide(int x, int y, Entity entity, Side side) {
        Tile tile = tileFromDirection(x, y, side);
        if (tile != null) {
            tile.onTouchSide(entity, null, side.opposite());
        }
    }

    public void activateAllTiles(Entity entity) {
        int x = (int) entity.position.x / 16;
        int y = (int) entity.position.y / 16;
        entityTouchedTileSide(x, y, entity, Side.UP);
        entityTouchedTileSide(x, y, entity, Side.DOWN);
        entityTouchedTileSide(x, y, entity, Side.LEFT);
        entityTouchedTileSide(x, y, entity, Side.RIGHT);
        if (isInMapRange(x, y)) {
//            Logger.debug(grid[x][y]);
//            Logger.debug(SlimeGame.getGame().getLevelHandler().getCurrentLevel().getId());
            grid[x][y].onCross(entity);
        }
    }

    public Tile[] getSurrounding(int x, int y) {
        List<Tile> tiles = new ArrayList<>();
        if (isInMapRange(x, y + 1)) {
            tiles.add(grid[x][y + 1]);
        }
        if (isInMapRange(x, y - 1)) {
            tiles.add(grid[x][y - 1]);
        }
        if (isInMapRange(x + 1, y)) {
            tiles.add(grid[x + 1][y]);
        }
        if (isInMapRange(x - 1, y)) {
            tiles.add(grid[x - 1][y]);
        }
        return tiles.toArray(new Tile[0]);
    }

    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public void update(float dTime) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].tick();
                }
            }
        }
    }
}
