package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.TriggerableTile;
import lombok.Getter;

@Getter
public class PressurePlate extends TriggerableTile {
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
    public void onCross(Entity entity) {
        if (!pressed) {
            if(entity.isPlayer() && ((JumpPlayer) entity).isSmall()){
                return;
            }
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();
            TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
            currentLevel.addEdit(x, y, currentLevel.getGameplayArtLayer().getName(), cell.getTile().getId());
            cell.setTile(currentLevel.getTileMap().getTileSets().getTile(cell.getTile().getId() + 1));
            callEvent(Trigger.ON_PASS);
            pressed = true;
        }
    }

}
