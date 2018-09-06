package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LevelEdit {
    public final int x;
    public final int y;
    public final String layer;
    public final int oldID;


    public void undo(TiledMap tiledMap){
        TiledMapTileLayer mapLayer = ((TiledMapTileLayer) tiledMap.getLayers().get(layer));
        mapLayer.getCell(x,y).setTile(tiledMap.getTileSets().getTile(oldID));
    }
}
