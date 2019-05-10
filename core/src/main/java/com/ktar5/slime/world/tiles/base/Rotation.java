package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.tilemap.CustomTmxMapLoader;

public enum Rotation {
    DEG_0,
    DEG_90,
    DEG_180,
    DEG_270;

    public static Rotation fromCell(TiledMapTileLayer.Cell cell) {
        CustomTmxMapLoader.CustomCell customCell = (CustomTmxMapLoader.CustomCell) cell;
        int rotation = customCell.getRealRotationBecauseFuckYouThatsWhy();
//        Logger.debug(rotation);
        Rotation value = Rotation.values()[rotation % 4];
//        Logger.debug(cell.getTile().getId());
//        Logger.debug(value);
        return value;
    }

}
