package com.ktar5.slime.entities.ghost;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LevelData;
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
        final LevelData levelData = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        //Get the position that we WOULD BE MOVING TO IF EVERYTHING GOES WELL so that we can use it
        //as a reference for where we want to go.
        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x), (SPEED * getMovement().y));

        //Initialize some integer variables to represent block locations of these variables
        //For example x/y are current x/y block and newX/newY are future x/y block
        int newX, newY;

        //Because of the nature of the gameTiles having the bottom left corner of each tile represent the
        //block integer location (tile 1,2 STARTS at the coordinates 1,2)
        //
        //This matters because when moving in a positive direction (+x = right, +y = up), flooring the
        //player coordinate gives us the correct tile coordinate, whereas the negative directions
        //(-x = left, -y = down) make use of ceiling the player coordinate
        if (getMovement() == Side.DOWN || getMovement() == Side.LEFT) {
            newX = (int) Math.ceil(newPosition.x / 16);
            newY = (int) Math.ceil(newPosition.y / 16);
        } else {
            newX = (int) Math.floor(newPosition.x / 16);
            newY = (int) Math.floor(newPosition.y / 16);
        }

        //This is the variable that stores the tile at the location of the next step
        //THIS COULD BE THE SAME TILE THE PLAYER IS CURRENTLY ON IF THE PLAYER
        //DOESN'T MOVE ENOUGH TO COVER THE DISTANCE INTO A NEW TILE THIS STEP
        //This is one block into the future, basically
        GameTile newGameTile = levelData.tileFromDirection(newX, newY, getMovement());
        if (newGameTile == null) {
            Logger.debug(newX + " " + newY);
            return;
        }

        JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
        if(player.isTouching(getEntity())){
            player.kill("ghost");
        }

        //In case we want to do something special instead of handle movement
        else if (!newGameTile.preMove(getEntity())) {

        }
        //In case we want to modify where the player is moving without setting them to idle
        else if (levelData.getGameMap()[newX][newY].changeMovement(getEntity(), getMovement())) {
            getEntity().getPosition().moveTo(newX * 16, newY * 16);
            getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
        }
        //Check for if the tile that the player WOULD BE GOING INTO is air or not
        else if (!newGameTile.canCrossThrough(getEntity(), getMovement())) {
            getEntity().getPosition().moveTo(newX * 16, newY * 16);
            getEntity().setPositive(!getEntity().isPositive());
        } else {
            getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
        }
    }


    @Override
    protected void end() {

    }

    public Side getMovement() {
        return getEntity().getMovement();
    }
}
