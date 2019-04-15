package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public abstract class LevelEdit {
    public final int x;
    public final int y;
    public final int oldID;

    protected LevelEdit(int x, int y, int oldID) {
        this.x = x;
        this.y = y;
        this.oldID = oldID;
    }

    public abstract void undo(TiledMap tiledMap);

    public static class IntLevelEdit extends LevelEdit {
        public final int layer;

        protected IntLevelEdit(int x, int y, int layer, int oldID) {
            super(x, y, oldID);
            this.layer = layer;
        }

        @Override
        public void undo(TiledMap tiledMap) {
            TiledMapTileLayer mapLayer = ((TiledMapTileLayer) tiledMap.getLayers().get(layer));
            if (oldID == 0)
                mapLayer.getCell(x, y).setTile(null);
            else
                mapLayer.getCell(x, y).setTile(tiledMap.getTileSets().getTile(oldID));

        }
    }

    public static class StringLevelEdit extends LevelEdit {
        public final String layer;

        protected StringLevelEdit(int x, int y, String layer, int oldID) {
            super(x, y, oldID);
            this.layer = layer;
        }

        @Override
        public void undo(TiledMap tiledMap) {
            TiledMapTileLayer mapLayer = ((TiledMapTileLayer) tiledMap.getLayers().get(layer));
            if (oldID == 0)
                mapLayer.getCell(x, y).setTile(null);
            else
                mapLayer.getCell(x, y).setTile(tiledMap.getTileSets().getTile(oldID));

        }
    }

}
