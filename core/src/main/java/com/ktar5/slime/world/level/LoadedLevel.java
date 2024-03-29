package com.ktar5.slime.world.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Updatable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelStartEvent;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.arrow.Arrow;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.tiles.base.GameTile;
import lombok.Getter;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class LoadedLevel extends LevelData implements Updatable {
    private JumpPlayer player;
    private ArrayList<LevelEdit> edits;
    private List<Entity> entities;
    private int collectibles = 0;
    private int numberTilesSlimed = 0;

    private int numberOfMoves = 0;

    public LoadedLevel(LevelData gameData) {
        super(gameData);
        this.edits = new ArrayList<>();

        this.entities = new ArrayList<>();
        for (EntityData initialEntityDatum : this.getInitialEntityData()) {
            entities.add(initialEntityDatum.create());
        }

        this.player = new JumpPlayer(this);
//        SlimeGame.getGame().getGameCamera().setPosition(player.position);
        SlimeGame.getGame().getGameCamera().setCameraLocations(getCameras());


        Analytics.addEvent(new LevelStartEvent(this));
    }

    @Override
    public void update(float dTime) {
        player.update(dTime);
        for (Entity entity : entities) {
            entity.update(dTime);
        }
        updateTiles(dTime);
    }

    public void playerMoved(){
        numberOfMoves += 1;
    }

    public void win(){
        Map<String, Integer> movesOnLevelMap = SlimeGame.getGame().getData().movesOnLevelMap;
        if(movesOnLevelMap.containsKey(getName())){
            Integer integer = movesOnLevelMap.get(getName());
            if(integer > numberOfMoves){
                movesOnLevelMap.put(getName(), numberOfMoves);
                SlimeGame.getGame().saveGame();
            }
        }else{
            movesOnLevelMap.put(getName(), numberOfMoves);
            SlimeGame.getGame().saveGame();
        }
    }

    public LevelEdit addEdit(int x, int y, int layer, int oldId) {
        LevelEdit levelEdit = new LevelEdit(x, y, layer, oldId);
        edits.add(levelEdit);
        return levelEdit;
    }

    public void reset() {
        numberOfMoves = 0;

        for (GameTile[] gameTiles : getGameMap()) {
            for (GameTile gameTile : gameTiles) {
                if (gameTile != null) {
                    gameTile.reset();
                }
            }
        }
//        for (int x = 0; x < getSlimeCovered().length; x++) {
//            for (int y = 0; y < getSlimeCovered()[x].length; y++) {
//                getSlimeCovered()[x][y] = false;
//            }
//        }
        for (LevelEdit edit : edits) {
            edit.undo(getRenderMap());
        }

        collectibles = 0;
        numberTilesSlimed = 0;

        for (Entity entity : entities) {
            if (entity instanceof Arrow){
                ((Arrow) entity).removeArrow();
            }
        }
        entities.clear();
        for (EntityData initialEntityDatum : this.getInitialEntityData()) {
            entities.add(initialEntityDatum.create());
        }
        player.reset();
    }

    public void incrementSlimeCovered() {
        numberTilesSlimed += 1;
    }

    /**Note that you must add the edit yourself
     * @returns the previous ID */
    public int setGraphic(int x, int y, String tileset, String layer, int id) {
        return setGraphic(x, y, tileset, (TiledMapTileLayer) this.getRenderMap().getLayers().get(layer), id);
    }

    /**Note that you must add the edit yourself
     * @returns the previous ID */
    public int setGraphic(int x, int y, String tileset, TiledMapTileLayer mapLayer, int id) {
        if(mapLayer == null){
            Logger.error("Could not file specified layer");
            return 1;
        }
        TiledMapTileSets tileSets = this.getRenderMap().getTileSets();
        int i = tileSets.getTileSet(tileset).getProperties().get("firstgid", Integer.class);
        if (mapLayer.getCell(x, y) == null || mapLayer.getCell(x,y).getTile() == null) {
            if(id == -1){
               return -1;
            }
            TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
            newCell.setTile(tileSets.getTile(i + id));
            mapLayer.setCell(x, y, newCell);
            return -1;
        } else {
            int firstID = mapLayer.getCell(x, y).getTile().getId();
            if(id == -1){
                mapLayer.getCell(x, y).setTile(null);
            }else{
                mapLayer.getCell(x, y).setTile(tileSets.getTile(i + id));
            }
            return firstID;
        }
    }

    public int getCurrentID(int x, int y, TiledMapTileLayer mapLayer) {
        if(mapLayer == null){
            throw new NullPointerException("The requested map layer doesn't exist");
        }
        if (mapLayer.getCell(x, y) == null) {
            return -1;
        } else {
            return mapLayer.getCell(x, y).getTile().getId();
        }
    }
}
