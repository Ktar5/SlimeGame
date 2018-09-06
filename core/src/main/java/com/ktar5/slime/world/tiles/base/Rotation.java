package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.slime.world.CustomTmxMapLoader;

public enum Rotation {
    DEG_0,
    DEG_90,
    DEG_180,
    DEG_270;

    public static Rotation fromCell(TiledMapTileLayer.Cell cell) {
        CustomTmxMapLoader.CustomCell customCell = (CustomTmxMapLoader.CustomCell) cell;
        int rotation = customCell.getRealRotationBecauseFuckYouThatsWhy();
        System.out.println(rotation);
        Rotation value = Rotation.values()[rotation % 4];
        System.out.println(cell.getTile().getId());
        System.out.println(value);
        return value;
    }

}
