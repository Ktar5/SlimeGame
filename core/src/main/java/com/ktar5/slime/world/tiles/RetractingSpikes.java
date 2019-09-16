package com.ktar5.slime.world.tiles;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.tweenengine.Timeline;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.gameengine.tweenengine.TweenAccessor;
import com.ktar5.gameengine.tweenengine.TweenCallback;
import com.ktar5.gameengine.tweenengine.equations.Linear;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeGameTile;
import org.tinylog.Logger;

public class RetractingSpikes extends WholeGameTile {
    public boolean retracted = true;
    public final Side spikeMoveSide;
    private int percentRetracted;

    public RetractingSpikes(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        spikeMoveSide = Side.DOWN.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void reset() {
        if(tween != null){
            tween.kill();
        }
        retracted = true;
        percentRetracted = 0;
        setGraphic(0);
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {
        if (entity.isPlayer()) {
            ((JumpPlayer) entity).kill("retracting_spikes");
        }
    }

    Timeline tween;
    @Override
    public boolean onCross(Entity entity) {
        if (retracted) {

            Logger.debug("Attempting to start tween");
            tween = Timeline.createSequence()
                    .pushPause(.2f)
                    .push(Tween.to(this, 0, 1).target(100).ease(Linear.INOUT))
                    .pushPause(1.0f)
                    .push(Tween.to(this, 1, 1).target(0).ease(Linear.INOUT))
                    .setCallback((type, source) -> {
                        Logger.debug("Tween ended");
                        this.percentRetracted = 0;
                    })
                    .setCallbackTriggers(TweenCallback.END)
                    .start(EngineManager.get().getTweenManager());
        }
        return false;
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return retracted;
    }

    public void updatePercentRetracted(int newValue, int lowering) {
        if (!this.retracted) {
            JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
            if (((int) (player.position.x / 16)) == this.x && ((int) (player.position.y / 16)) == this.y) {
                player.kill("retracting_spikes");
            }
        }

        if (lowering == 1) { //This means that new value is going from 100 to 0
            //TODO figure out how we want the speed of the animation to be
            setGraphic((newValue - 2) / 3);
            if (newValue == 0) {
                retracted = true;
            }
        } else { //This means that new value is going from 0 to 100
            //TODO figure out how we want the speed of the animation to be
            setGraphic((newValue - 2) / 3);
            retracted = false;
//            if(newValue > 50){
//                retracted = false;
//            }
        }

        this.percentRetracted = newValue;
    }

    private void setGraphic(int i) {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        TiledMapTileSet gameplayImages = currentLevel.getRenderMap().getTileSets().getTileSet("sprites");
        int firstGid = gameplayImages.getProperties().get("firstgid", Integer.class);
        int id = firstGid + 226 + i;
        if (id > firstGid + 226 + 2) {
            id = firstGid + 226 + 2;
        }
        TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();

        if (mapLayer.getCell(x, y) == null) {
            TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
            newCell.setTile(gameplayImages.getTile(id));
            mapLayer.setCell(x, y, newCell);
        } else {
            mapLayer.getCell(x, y).setTile(gameplayImages.getTile(id));
        }
    }

    public static class SpikesTweenAccessor implements TweenAccessor<RetractingSpikes> {

        @Override
        public int getValues(RetractingSpikes target, int tweenType, float[] returnValues) {
            returnValues[0] = target.percentRetracted;
            return 1;
        }

        @Override
        public void setValues(RetractingSpikes target, int tweenType, float[] newValues) {
            target.updatePercentRetracted((int) newValues[0], tweenType);
        }
    }

}
