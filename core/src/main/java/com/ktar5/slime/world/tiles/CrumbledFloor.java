package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class CrumbledFloor extends WholeTile {
    public boolean crumbled = false;

    public CrumbledFloor(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        crumbled = false;
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void onCross(Entity entity) {
        if (crumbled) {
            if (entity.isPlayer()) {
                ((JumpPlayer) entity).kill("crumbled_floor");
            }
        } else {

            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();
            TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
            currentLevel.addEdit(x, y, currentLevel.getGameplayArtLayer().getName(), cell.getTile().getId());
            cell.setTile(currentLevel.getTileMap().getTileSets().getTile(cell.getTile().getId() + 1));

            crumbled = true;
        }
    }
}
