package com.ktar5.slime.world;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import lombok.Getter;
import lombok.Setter;

public class CustomTmxMapLoader extends TmxMapLoader {

    public static class CustomCell extends TiledMapTileLayer.Cell {
        @Getter
        @Setter
        private int realRotationBecauseFuckYouThatsWhy;
    }

    @Override
    protected TiledMapTileLayer.Cell createTileLayerCell(boolean flipHorizontally, boolean flipVertically, boolean flipDiagonally) {
        CustomCell cell = new CustomCell();
        if (flipDiagonally) {
            if (flipHorizontally && flipVertically) {
                cell.setFlipHorizontally(true);
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
                cell.setRealRotationBecauseFuckYouThatsWhy(TiledMapTileLayer.Cell.ROTATE_270);
            } else if (flipHorizontally) {
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
                cell.setRealRotationBecauseFuckYouThatsWhy(TiledMapTileLayer.Cell.ROTATE_90);
            } else if (flipVertically) {
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_90);
                cell.setRealRotationBecauseFuckYouThatsWhy(TiledMapTileLayer.Cell.ROTATE_270);
            } else {
                cell.setFlipVertically(true);
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
                cell.setRealRotationBecauseFuckYouThatsWhy(TiledMapTileLayer.Cell.ROTATE_0);
            }
        } else {
            if (flipVertically){
                cell.setRealRotationBecauseFuckYouThatsWhy(TiledMapTileLayer.Cell.ROTATE_180);
            }
            cell.setFlipHorizontally(flipHorizontally);
            cell.setFlipVertically(flipVertically);
        }
        return cell;
    }

}
