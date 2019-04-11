package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.world.Grid;
import com.ktar5.slime.world.tiles.base.Tile;

import java.util.List;

public class ArrowMove extends ArrowState {
    private static final float SPEED = 1f;

    protected ArrowMove(Arrow entity) {
        super(entity);
    }

    @Override
    public void start() {
        if (getEntity().currentMovement == null) {
            end();
            System.out.println("broken arrows!!!!!!!!!!!!");
            return;
        }

        Vector2 input = new Vector2((int) Math.ceil(getEntity().currentMovement.x),
                (int) Math.ceil(getEntity().currentMovement.y));
        //Make sure we only move in ONE direction (x or y)
        if (input.x != 0)
            input.set(input.x, 0);
        else
            input.set(0, input.y);

        //Reset the last moved direction
        getEntity().setLastMovedDirection(Side.of((int) input.x, (int) input.y));
    }

    @Override
    public void onUpdate(float dTime) {
        if (getEntity().isHaltMovement()) {
            return;
        }
        final Grid grid = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid();

        //Get the position that we WOULD BE MOVING TO IF EVERYTHING GOES WELL so that we can use it
        //as a reference for where we want to go.
        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x), (SPEED * getMovement().y));

        //Initialize some integer variables to represent block locations of these variables
        //For example x/y are current x/y block and newX/newY are future x/y block
        int newX, newY;

        //Because of the nature of the grid having the bottom left corner of each tile represent the
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
        Tile newTile = grid.tileFromDirection(newX, newY, getMovement());
        if (newTile == null) {
            System.out.println(newX + " " + newY);
            return;
        }

        List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
        boolean touchedEntity = false;
        for (Entity entity : entities) {
            if (entity.position.equals(newTile.x * 16, newTile.y * 16)) {
                if(entity instanceof Arrow){
                    return;
                }
                ((TouchableEntity) entity).onEntityTouch(getEntity(), getEntity().getLastMovedDirection());
                if(entity instanceof JumpPlayer){
                    ((JumpPlayer) entity).kill();
                }
                touchedEntity = true;
                break;
            }
        }

        if (touchedEntity) {
            EngineManager.get().getRenderManager().doOnNextFrame(() -> {
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(getEntity());
            });
            return;
        }

        //In case we want to do something special instead of handle movement
        else if (!newTile.preMove(getEntity())) {

        }
        //In case we want to modify where the player is moving without setting them to idle
        else if (grid.grid[newX][newY].changeMovement(getEntity(), getMovement())) {
            getEntity().getPosition().moveTo(newX * 16, newY * 16);
            getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
        }
        //Check for if the tile that the player WOULD BE GOING INTO is air or not
        else if (!newTile.canCrossThrough(getEntity(), getMovement())) {
            //If it is not air, then that means we have reached a wall
            //This little bit of logic (moving to newX, newY) works because
            //if their next movement would've been to a wall, that means they're
            //right next to a wall, so we just snap them to THE BLOCK
            // AND NOT THE *POSITION* of the new movement
            //
            //Ex: going to hit a wall, so snap them to block before wall
            getEntity().getPosition().moveTo(newX * 16, newY * 16);

//            //Change state to idle
//            changeState(ArrowIdle.class);

            newTile.onHitTile(getEntity(), getMovement().opposite());
            System.out.println("arrow hitwall and die");
            EngineManager.get().getRenderManager().doOnNextFrame(() -> {
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(getEntity());
            });
        }
        //This is for regular movement
        else {
            //Translate the player's location by SPEED multiplied by the movement direction
            getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
        }
    }


    @Override
    protected void end() {

    }

    public Side getMovement() {
        return getEntity().getLastMovedDirection();
    }
}
