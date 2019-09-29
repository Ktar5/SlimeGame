package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;
import lombok.Getter;

@Getter
public class Gate extends WholeGameTile {
    public final Side opening;
    private boolean open = false;

    private static final int closedID = 2;

    public Gate(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        opening = Side.DOWN.rotateClockwise(rotation.ordinal());
    }

    private int basicClosedID = 0;

    public void setOpen(boolean value) {
        if (value == open) {
            return;
        }
        open = value;
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        if (open) {
            basicClosedID = ((TiledMapTileLayer) currentLevel.getRenderMap().getLayers().get("Art_Gameplay")).getCell(x, y).getTile().getId();
            currentLevel.addEdit(x, y, currentLevel.getGameplayArtLayerIndex(), basicClosedID);

            basicClosedID = basicClosedID - currentLevel.getRenderMap().getTileSets().getTileSet("sprites").getProperties().get("firstgid", Integer.class);
            currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), -1);
        } else {
            currentLevel.setGraphic(x, y, "sprites", currentLevel.getGameplayArtLayer(), basicClosedID);
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
