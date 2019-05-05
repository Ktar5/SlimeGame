package com.ktar5.tileeditor.tilemap.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.TilemapActor;
import com.ktar5.tileeditor.tileset.Tile;
import com.ktar5.tileeditor.tileset.Tileset;
import com.ktar5.utilities.common.constants.Direction;
import lombok.AccessLevel;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

import java.util.Optional;

@Getter
public class TileLayer extends BaseLayer {
    @Getter(AccessLevel.NONE)
    protected Tile[][] grid;

    public TileLayer(Tilemap parent, JSONObject json) {
        super(parent, json);
        grid = new Tile[parent.getNumTilesWide()][parent.getNumTilesHigh()];
        JSONArray jsonArray = json.getJSONArray("tiles");
        String[][] blocks = new String[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            blocks[i] = jsonArray.getString(i).split(",");
        }

        //Blocks will load from top down
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                /*
                Note here that we switch the x and y variable because we store the data as
                an array of ROWS, and we load it as ROWS, so the first-dimension array is the ROWS
                and the 2nd dimension of the array is the COLUMNS
                See comments in Tilemap#getJsonArray on the line with:
                jsonArray.put(y, builder.toString());
                Hence, data is stored "y-value, row string"
                */
                /*
                We need to flip it because the 0th row read from the file (top down)
                is actually the top row of the tileset
                 */
                int flippedY = getParent().getNumTilesHigh() - 1 - y;
                deserializeBlock(blocks[y][x], x, flippedY);
            }
        }
    }

    public TileLayer(Tilemap parent, String name, boolean visible, int xOffset, int yOffset) {
        super(parent, name, visible, xOffset, yOffset);
        this.grid = new Tile[getParent().getNumTilesWide()][getParent().getNumTilesHigh()];
    }

    @Override
    public void render(Batch batch, TilemapActor actor) {
        if (!isVisible()) {
            return;
        }
        batch.setColor(1, 1, 1, getTransparency() / 100f);
        float scale = actor.getScale();
        for (int row = getParent().getNumTilesHigh() - 1; row >= 0; row--) {
            for (int col = 0; col <= getParent().getNumTilesWide() - 1; col++) {
                if (grid[col][row] == null) {
                    continue;
                }
//                int blockId = grid[col][row].getBlockId();
//                if (blockId == 0) {
//                    continue;
//                }

                TextureRegion tileImage = grid[col][row].getTextureRegion();
                float x = actor.getRenderX() + (col * getParent().getTileWidth() * scale);
                float y = actor.getRenderY() + (row * getParent().getTileHeight() * scale);
                x += (getParent().getTileWidth() / 2f) * (scale - 1);
                y += (getParent().getTileHeight() / 2f) * (scale - 1);
                batch.draw(tileImage, (int) x, (int) y,
                        tileImage.getRegionWidth() / 2, tileImage.getRegionHeight() / 2,
                        getParent().getTileWidth(), getParent().getTileHeight(),
                        scale, scale,
                        grid[col][row].getDirection() * 90);
            }
        }
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public JSONObject serialize() {
        JSONObject superJsonObject = super.serialize();
        JSONArray jsonArray = new JSONArray();
        StringBuilder builder = new StringBuilder();
        for (int y = getParent().getNumTilesHigh() - 1; y >= 0; y--) {
            for (int x = 0; x <= getParent().getNumTilesWide() - 1; x++) {
                if (grid[x][y] == null) {
                    builder.append("0");
                } else {
                    builder.append(getParent().getTilesets().getIndexByTileset(grid[x][y].getTileset()))
                            .append("_").append(grid[x][y].serialize());
                }
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            jsonArray.put(y, builder.toString());
            builder.setLength(0);
        }
        superJsonObject.put("class", this.getClass().getName());
        return superJsonObject.put("tiles", jsonArray);
    }

    /**
     * This method is called to deserialize each comma-separated string in the
     * "layer" section of the serialized json file
     * <p>
     * layer_id_rotation
     *
     * @param block the string representing a single element of the comma-separated string in
     *              the serialized json file
     */
    private void deserializeBlock(String block, int x, int y) {
        if (!block.equals("0")) {
            String[] split = block.split("_");
            if (split.length == 2) {
                this.grid[x][y] = new Tile(Integer.valueOf(split[1]), 0,
                        getParent().getTilesets().getTileset(Integer.valueOf(split[0])));
            } else if (split.length == 3) {
                this.grid[x][y] = new Tile(Integer.valueOf(split[1]), Integer.valueOf(split[2]),
                        getParent().getTilesets().getTileset(Integer.valueOf(split[0])));
            } else {
                Logger.debug("Length > 3 error");
            }
        }
    }

    public Optional<Tile> tileFromPoint(int x, int y) {
        if (!parent.isInMapRange(x, y)) {
            throw new RuntimeException("Point (" + x + ", " + y + ") is not in the bounds of map: " + parent.getName());
        }
        x = (x - xOffset) / (getParent().getTileWidth());
        y = (y - yOffset) / (getParent().getTileHeight());
        if (x < 0 || y < 0)
            return Optional.empty();
        else
            return Optional.of(grid[x][y]);
    }

    public void setTile(int blockId, int direction, Tileset tileset, int x, int y) {
        this.setTile(new Tile(blockId, direction, tileset), x, y);
    }

    public void setTile(Tile tile, int x, int y) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            Logger.debug("Tried setting tile outside of bounds, ignored.");
            return;
        }
        grid[x][y] = tile;
    }

    public void removeTile(int x, int y) {
        setTile(null, x, y);
    }

    @Deprecated
    public void expandMap(int n, Direction direction) {
        int heightTmpMap = parent.getDimensionX() + (direction.y * n);
        int widthTmpMap = parent.getDimensionY() + (direction.x * n);
        Tile[][] tilemap = new Tile[widthTmpMap][heightTmpMap];

        // Copy the old map's data to the new one.
        for (int y = 0; y < grid.length; y++) {
            //TODO might need to fix
            System.arraycopy(grid[y], 0, tilemap[y + direction.y * n], direction.x * n, grid[0].length);
        }
        parent.setChanged(true);
    }

}
