package com.ktar5.slime.player.states;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.Feature;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.world.grid.Grid;
import com.ktar5.slime.world.grid.tiles.Tile;
import org.pmw.tinylog.Logger;

public class Move extends PlayerState {
    private static final float SPEED = .5f;
    int blocksMoved = 0;
    
    @Override
    public void start() {
        Vector2 input = getPlayer().getMovement().getInput();
        input.set((int) Math.ceil(input.x), (int) Math.ceil(input.y));
        
        if (input.equals(Vector2.Zero)) {
            end();
        }
        
        if (input.x != 0) input.set(input.x, 0);
        else input.set(0, input.y);
        getPlayer().setLastMovedDirection(Side.of((int) input.x, (int) input.y));
        blocksMoved = 0;
        getPlayer().getEntityAnimator().setManualAnimation(EngineManager.get().getAnimationLoader().getAnimation("slime_jump_"
                + getMovement().name().toLowerCase()), 0);
    }
    
    @Override
    public void onUpdate(float dTime) {
        final Grid grid = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid();
        
        //Get the position that we WOULD BE MOVING TO IF EVERYTHING GOES WELL so that we can use it
        //as a reference for where we want to go.
        Vector2 newPosition = getPlayer().getPosition().cpy().add((SPEED * getMovement().x), (SPEED * getMovement().y));
        
        //Initialize some integer variables to represent block locations of these variables
        //For example x/y are current x/y block and newX/newY are future x/y block
        int x, y, newX, newY;
        
        //Because of the nature of the grid having the bottom left corner of each tile represent the
        //block integer location (tile 1,2 STARTS at the coordinates 1,2)
        //
        //This matters because when moving in a positive direction (+x = right, +y = up), flooring the
        //player coordinate gives us the correct tile coordinate, whereas the negative directions
        //(-x = left, -y = down) make use of ceiling the player coordinate
        if (getMovement() == Side.DOWN || getMovement() == Side.LEFT) {
            x = (int) Math.ceil(getPlayer().getPosition().x);
            y = (int) Math.ceil(getPlayer().getPosition().y);
            newX = (int) Math.ceil(newPosition.x);
            newY = (int) Math.ceil(newPosition.y);
        } else {
            x = (int) Math.floor(getPlayer().getPosition().x);
            y = (int) Math.floor(getPlayer().getPosition().y);
            newX = (int) Math.floor(newPosition.x);
            newY = (int) Math.floor(newPosition.y);
        }
        
        if (Feature.LOG_MOVEMENT.isEnabled()) {
            Logger.debug("");
            Logger.debug("Old: " + x + ", " + y + " // " + getPlayer().getPosition().x + ", " + getPlayer().getPosition().y);
        }
        //This is the variable that stores the tile at the location of the next step
        //THIS COULD BE THE SAME TILE THE PLAYER IS CURRENTLY ON IF THE PLAYER
        //DOESN'T MOVE ENOUGH TO COVER THE DISTANCE INTO A NEW TILE THIS STEP
        //This is one block into the future, basically
        Tile newTile = grid.tileFromDirection(newX, newY, getMovement());
    
        //In case we want to modify where the player is moving without setting them to idle
        if(grid.grid[newX][newY].changeMovement(getPlayer(), getMovement())){
            //TODO test to see if this counts as multiple movements D:
            getPlayer().getPosition().moveTo(newX, newY);
            getPlayer().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
        }
        //Check for if the tile that the player WOULD BE GOING INTO is air or not
        else if (!newTile.canCrossThrough(getPlayer(), getMovement())) {
            //If it is not air, then that means we have reached a wall
            //This little bit of logic (moving to newX, newY) works because
            //if their next movement would've been to a wall, that means they're
            //right next to a wall, so we just snap them to THE BLOCK
            // AND NOT THE *POSITION* of the new movement
            //
            //Ex: going to hit a wall, so snap them to block before wall
            getPlayer().getPosition().moveTo(newX, newY);
            
            //Change state to idle
            changeState(Idle.class);
            getPlayer().getEntityAnimator().setFrame(3);
            
            newTile.onPlayerHitTile(getPlayer(), getMovement().opposite());
        }
        //This is for regular movement
        else {
            //Translate the player's location by SPEED multiplied by the movement direction
            getPlayer().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
        }
        
        //Keeps count of tiles crossed for the animation
        if (newX != x || newY != y) {
            blocksMoved++;
            getPlayer().getEntityAnimator().setFrame(1);
        }
        
        if (Feature.LOG_MOVEMENT.isEnabled()) {
            Logger.debug("New: " + newX + ", " + newY + " // " + newPosition.x + ", " + newPosition.y);
            Logger.debug("Blocks moved: " + blocksMoved);
        }
    }
    
    
    @Override
    protected void end() {
    
    }
    
    public Side getMovement() {
        return getPlayer().getLastMovedDirection();
    }
    
}