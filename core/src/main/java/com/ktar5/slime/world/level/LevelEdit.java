package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class LevelEdit {
    public final int x;
    public final int y;
    public final int oldID;
    public final int layer;

    LevelEdit(int x, int y, int layer, int oldID) {
        this.x = x;
        this.y = y;
        this.oldID = oldID;
        this.layer = layer;

    }

    public void undo(TiledMap tiledMap) {
        TiledMapTileLayer mapLayer = ((TiledMapTileLayer) tiledMap.getLayers().get(layer));
        if (oldID == 0)
            mapLayer.getCell(x, y).setTile(null);
        else
            mapLayer.getCell(x, y).setTile(tiledMap.getTileSets().getTile(oldID));

    }

}
