package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;

public class Collectible extends WholeGameTile {
    boolean collected = false;

    public Collectible(int x, int y) {
        super(x, y, Rotation.DEG_0);
    }

    @Override
    public void reset() {
        collected = false;
    }


    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return true;
    }

    @Override
    public void onCross(Entity entity) {
        if(!collected){
            collected = true;
            LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
            TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();
            currentLevel.addEdit(x, y, currentLevel.getRenderMap().getLayers().getIndex("Art_Gameplay"), 791);
            mapLayer.getCell(x,y).setTile(null);
        }
    }


}