package com.ktar5.slime.world;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.Tile;

import java.util.ArrayList;
import java.util.List;

public class Grid implements Updatable {
    public final int width, height;
    public Tile[][] grid;
    public List<Entity> entities;
    //TODO check performance for ticks and see if ticking all has any noticeable performance impact

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
//                    grid[x][y] = new WholeTile(x, y, TileType.WALL);
//                } else {
//                    grid[x][y] = new WholeTile(x, y, TileType.AIR);
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

    private void playerTouchSideOfTile(int x, int y, JumpPlayer player, Side side) {
        Tile tile = tileFromDirection(x, y, side);
        if (tile != null) {
            tile.onPlayerTouchSide(player, null, side.opposite());
        }
    }

    public void activateAllTiles(JumpPlayer player) {
        int x = (int) player.position.x;
        int y = (int) player.position.y;
        playerTouchSideOfTile(x, y, player, Side.UP);
        playerTouchSideOfTile(x, y, player, Side.DOWN);
        playerTouchSideOfTile(x, y, player, Side.LEFT);
        playerTouchSideOfTile(x, y, player, Side.RIGHT);
        if (isInMapRange(x, y)) {
            grid[x][y].onPlayerCross(player);
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
                grid[x][y].tick();
            }
        }
    }
}
