package com.ktar5.slime.world.tiles;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.tweenengine.Timeline;
import com.ktar5.slime.engine.tweenengine.Tween;
import com.ktar5.slime.engine.tweenengine.TweenAccessor;
import com.ktar5.slime.engine.tweenengine.TweenCallback;
import com.ktar5.slime.engine.tweenengine.equations.Linear;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.slime.world.tiles.base.WholeTile;

public class RetractingSpikes extends WholeTile {
    public boolean retracted = true;
    public final Side spikeMoveSide;
    private int percentRetracted;
    private Timeline tween;

    //TODO add to TileObjectType
    public RetractingSpikes(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        spikeMoveSide = Side.DOWN.rotateClockwise(rotation.ordinal());
        tween = Timeline.createSequence()
                .push(Tween.to(this, 0, 1).target(100).ease(Linear.INOUT))
                .pushPause(1.0f)
                .push(Tween.to(this, 1, 1).target(0).ease(Linear.INOUT))
                .setCallback((type, source) -> this.percentRetracted = 0)
                .setCallbackTriggers(TweenCallback.END);
    }

    @Override
    public void reset() {
        retracted = true;
        percentRetracted = 0;
        tween.end();
    }

    @Override
    public void onHitTile(Entity entity, Side hit) {
        if (entity.isPlayer()) {
            ((JumpPlayer) entity).kill();
        }
    }

    @Override
    public void onCross(Entity entity) {
        if (retracted) {
            tween.start();
        }
    }

    @Override
    public boolean canCrossThrough(Entity entity, Side movement) {
        return retracted;
    }

    public void updatePercentRetracted(int newValue, int lowering) {
        if (lowering == 1) { //This means that new value is going from 100 to 0
            //TODO figure out how we want the speed of the animation to be
        } else { //This means that new value is going from 0 to 100
            //TODO figure out how we want the speed of the animation to be
        }

        this.percentRetracted = newValue;
    }

    private void setTextureOfTile(int id) {
        LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
        currentLevel.getGameplayArtLayer().getCell(x, y).setTile(currentLevel.getTileMap().getTileSets().getTile(id));
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
