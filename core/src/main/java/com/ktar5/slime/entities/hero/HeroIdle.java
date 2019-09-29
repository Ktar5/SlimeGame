package com.ktar5.slime.entities.hero;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.*;
import com.ktar5.slime.world.tiles.base.GameTile;

import java.util.HashSet;
import java.util.Set;

public class HeroIdle extends HeroState {
    public HeroIdle(HeroEntity entity) {
        super(entity);
    }

    @Override
    public void start() {

    }

    //cannot see through: box, closed gate, wall, one-dir, tiny, spike,
    @Override
    public void onUpdate(float dTime) {
        final LoadedLevel levelData = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        //Because of the nature of the gameTiles having the bottom left corner of each tile represent the
        //block integer location (tile 1,2 STARTS at the coordinates 1,2)
        int heroTileX = (int) Math.floor(getEntity().getPosition().x / 16);
        int heroTileY = (int) Math.floor(getEntity().getPosition().y / 16);

        int playerTileX = (int) Math.floor(levelData.getPlayer().getPosition().x / 16);
        int playerTileY = (int) Math.floor(levelData.getPlayer().getPosition().y / 16);

        //For optimization purposes, make sure that the player is at least in the same row or column
        //as the hero
        boolean playerInLineOfSight = false;
        switch (getEntity().facingDirection) {
            case UP:
                playerInLineOfSight = playerTileX == heroTileX && playerTileY > heroTileY;
                break;
            case RIGHT:
                playerInLineOfSight = playerTileX > heroTileX && playerTileY == heroTileY;
                break;
            case DOWN:
                playerInLineOfSight = playerTileX == heroTileX && playerTileY < heroTileY;
                break;
            case LEFT:
                playerInLineOfSight = playerTileX < heroTileX && playerTileY == heroTileY;
                break;
        }
        //Stop the search if the player isn't within the same col/row
        if (!playerInLineOfSight) {
            return;
        }

        //Player is within the same col/row so start looking for them!

        //Store all boxes for later, quicker iteration
        Set<Box> boxSet = new HashSet<>();
        for (Entity entity : levelData.getEntities()) {
            if (entity instanceof Box) {
                if (((Box) entity).currentMovement == null) {
                    boxSet.add(((Box) entity));
                }
            }
        }

        GameTile[][] gameMap = levelData.getGameMap();

        int currentTileX = heroTileX;
        int currentTileY = heroTileY;

        int boxX, boxY;
        while (true) {
            currentTileX += getEntity().facingDirection.x;
            currentTileY += getEntity().facingDirection.y;

            //cannot see through: box, closed gate, wall, one-dir, tiny, spike,
            GameTile tile = gameMap[currentTileX][currentTileY];
            if (tile instanceof Wall || tile instanceof HoleInWall || tile instanceof Spikes) {
                return;
            } else if (tile instanceof Gate) {
                Gate gate = ((Gate) tile);
                if (gate.isOpen()) {
                    if (!gate.opening.equals(getEntity().facingDirection) && !gate.opening.opposite().equals(getEntity().facingDirection)) {
                        return;
                    }
                } else {
                    return;
                }
            } else if (tile instanceof OneDirection) {
                OneDirection oneDirection = ((OneDirection) tile);
                if (!oneDirection.allowedDirection.opposite().equals(getEntity().facingDirection)) {
                    return;
                }
            } else {
                for (Box box : boxSet) {
                    boxX = (int) Math.floor(box.getPosition().x / 16);
                    boxY = (int) Math.floor(box.getPosition().y / 16);
                    if (boxX == currentTileX && boxY == currentTileY) {
                        return;
                    }
                }
            }

            //Would have returned if there was anything blocking the path, now we check player
            if (playerTileX == currentTileX && playerTileY == currentTileY) {
                //The player is in visible, walkable area
                changeState(HeroMove.class);
                return;
            }

        }


    }

    @Override
    protected void end() {

    }
}
