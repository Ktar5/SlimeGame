package com.ktar5.slime.entities.ghost;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.world.Grid;
import com.ktar5.slime.world.tiles.base.Tile;

public class GhostMove extends GhostState {
    private static final float SPEED = 2f;

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

//        List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
//        boolean touchedEntity = false;
//        for (Entity entity : entities) {
//            if (entity.position.equals(newTile.x * 16, newTile.y * 16)) {
//                ((TouchableEntity) entity).onEntityTouch(getEntity(), getEntity().getLastMovedDirection());
//                touchedEntity = true;
//                break;
//            }
//        }

//        if (touchedEntity) {
//            getEntity().getPosition().moveTo(newX * 16, newY * 16);
//            changeState(BoxIdle.class);
//        }

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
