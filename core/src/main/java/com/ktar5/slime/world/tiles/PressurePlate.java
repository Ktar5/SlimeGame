package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableGameTile;
import lombok.Getter;

@Getter
public class PressurePlate extends TriggerableGameTile {
    private boolean pressed = false;

    public PressurePlate(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void reset() {
        pressed = false;
    }

    @Override
    public boolean onCross(Entity entity) {
        if (!pressed) {
            if(entity.isPlayer() && ((JumpPlayer) entity).isSmall()){
                return false;
            }
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();
            TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
            currentLevel.addEdit(x, y, currentLevel.getGameplayArtLayerIndex(), cell.getTile().getId());
            cell.setTile(currentLevel.getRenderMap().getTileSets().getTile(cell.getTile().getId() + 1));
            callEvent(Trigger.ON_PASS);

            pressed = true;
        }
        return false;
    }

}
