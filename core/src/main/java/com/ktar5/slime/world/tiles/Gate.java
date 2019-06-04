package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LevelEdit;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Gate extends WholeGameTile {
    public final Side opening;
    private boolean open = false;
    private LevelEdit edit;

    public Gate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        opening = Side.DOWN.rotateClockwise(rotation.ordinal());
    }

    public void setOpen(boolean value) {
        if(value == open){
            return;
        }
        open = value;
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();
        TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
        if (open) {
            edit = currentLevel.addEdit(x, y, currentLevel.getGameplayArtLayerIndex(), cell.getTile().getId());
            cell.setTile(currentLevel.getRenderMap().getTileSets().getTile(0));
        } else if (edit != null) {
            edit.undo(currentLevel.getRenderMap());
        }
    }

    @Override
    public void reset() {
        open = false;
    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return open && (movement.opposite().equals(opening) || movement.equals(opening));
    }
}
