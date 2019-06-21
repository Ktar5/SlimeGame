package com.ktar5.slime.entities.ghost;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.GameTile;
import org.tinylog.Logger;

public class GhostMove extends GhostState {
    private static final float SPEED = Settings.GHOST_MOVE_SPEED;

    protected GhostMove(Ghost entity) {
        super(entity);
    }

    @Override
    public void start() {

    }

    @Override
    public void onUpdate(float dTime) {
        if (getEntity().isHaltMovement()) {
            return;
        }
        final LoadedLevel levelData = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x), (SPEED * getMovement().y));

        JumpPlayer player = levelData.getPlayer();
        if (player.isTouching(getEntity())) {
            player.kill("ghost");
        }

        int currentTileX = (int) Math.floor(getEntity().getPosition().x / 16);
        int currentTileY = (int) Math.floor(getEntity().getPosition().y / 16);

        int pixelsThroughTile, futurePixelsThroughTile;
        if (getMovement().equals(Side.UP)) {
            pixelsThroughTile = (int) (getEntity().getPosition().y - (currentTileY * 16));
            futurePixelsThroughTile = (int) (newPosition.y - (currentTileY * 16));
        } else if (getMovement().equals(Side.DOWN)) {
            pixelsThroughTile = (int) (16 + (currentTileY * 16) - getEntity().getPosition().y);
            futurePixelsThroughTile = (int) (16 + (currentTileY * 16) - newPosition.y);
        } else if (getMovement().equals(Side.LEFT)) {
            pixelsThroughTile = (int) (16 + (currentTileX * 16) - getEntity().getPosition().x);
            futurePixelsThroughTile = (int) (16 + (currentTileX * 16) - newPosition.x);
        } else if (getMovement().equals(Side.RIGHT)) {
            pixelsThroughTile = (int) (getEntity().getPosition().x - (currentTileX * 16));
            futurePixelsThroughTile = (int) (newPosition.x - (currentTileX * 16));
        } else {
            Logger.error(new RuntimeException("Movement is not of up, down, left, or right."));
            return;
        }


        if (pixelsThroughTile < 8 && futurePixelsThroughTile >= 8) {
            GameTile nextTile = levelData.tileFromDirection(currentTileX, currentTileY, getMovement());
            if (!nextTile.canCrossThrough(getEntity(), getMovement())) {
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getEntity().setPositive(!getEntity().isPositive());
                return;
            }
        }
        getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
    }


    @Override
    protected void end() {

    }

    public Side getMovement() {
        return getEntity().getMovement();
    }
}
