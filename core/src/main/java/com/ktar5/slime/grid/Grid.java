package com.ktar5.slime.grid;

import com.ktar5.utilities.common.constants.Direction;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    public Tile[][] grid;
    public final int width, height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        initialize();
    }

    public void initialize() {
        this.grid = new Tile[width][height];
        if (loadGrid()) {
            return;
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    grid[x][y] = new Tile(x, y, TileType.WALL);
                }else{
                    grid[x][y] = new Tile(x, y, TileType.AIR);
                }
            }
        }
    }

    public Tile tileFromDirection(int x, int y, Direction direction) {
        x += direction.x;
        y += direction.y;
        if (isInMapRange(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    public Tile[] getSurrounding(int x, int y) {
        List<Tile> tiles = new ArrayList<Tile>();
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
        return tiles.toArray(new Tile[tiles.size()]);
    }

    public boolean isInMapRange(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    //Load grid from a file created by SaveGrid()
    boolean loadGrid() {
        return false;
    }

    //Save current grid to a file
    boolean saveGrid() {
        return false;
    }

    public void tickAll() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //TODO
            }
        }
    }


}
