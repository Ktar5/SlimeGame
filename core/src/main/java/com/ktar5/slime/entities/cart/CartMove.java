package com.ktar5.slime.entities.cart;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LevelData;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.Spikes;
import com.ktar5.slime.world.tiles.base.GameTile;
import org.tinylog.Logger;

import java.util.List;

public class CartMove extends CartState {
    private static final float SPEED = Settings.CART_MOVE_SPEED;
    private boolean isBeginning = false;

    protected CartMove(Cart entity) {
        super(entity);
    }

    @Override
    public void start() {

        int tileX = (int) getEntity().getPosition().x / 16;
        int tileY = (int) getEntity().getPosition().y / 16;
        if(SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGameMap()[tileX][tileY] instanceof Spikes){
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().kill("spikes");
        }else{
            getEntity().getPosition().add(getMovement().x, getMovement().y);
        }


        isBeginning = true;
        if (getEntity().currentMovement == null) {
            end();
            changeState(CartIdle.class);
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

        final LevelData levelData = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        int currentTileX = (int) Math.floor(getEntity().getPosition().x / 16);
        int currentTileY = (int) Math.floor(getEntity().getPosition().y / 16);

        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x), (SPEED * getMovement().y));

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

        if (isBeginning || (pixelsThroughTile < 8 && futurePixelsThroughTile >= 8)) {
            isBeginning = false;
            GameTile nextTile = levelData.tileFromDirection(currentTileX, currentTileY, getMovement());
            GameTile currentTile = levelData.getGameMap()[currentTileX][currentTileY];
            if (nextTile == null) {
                int x = ((int) newPosition.x / 16);
                int y = ((int) newPosition.y / 16);
                Logger.error("Null tile at: " + x + ", " + y + ". Adjusted Y-value: " + (levelData.getGameMap()[0].length - y));
                levelData.getGameMap()[x][y] = nextTile = new Air(x, y);
            }

//            if (!wasBeginning && SlimeGame.getGame().getLevelHandler().getCurrentLevel().activateAllTiles(getEntity())) {
//                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
//                return;
//            }

            //TODO bug: cart hits box, box hits corner, cart cannot continue? idk if this is bug
            List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
            Vector2 futureAheadHitboxPosition = newPosition.cpy().add(getMovement().x * 10, getMovement().y * 10);
            boolean shouldStop = false;
            JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
            if(player.isTouching(getEntity().getHitbox(), futureAheadHitboxPosition)){
                //TODO TEST
                getEntity().onTouchedByEntity(player, player.getLastMovedDirection());
            }
            for (Entity entity : entities) {
                if (entity.equals(getEntity())) {
                    continue;
                }
                if (entity instanceof GameEntity && ((GameEntity) entity).isTouching(getEntity().getHitbox(), futureAheadHitboxPosition)) {
                    if (entity instanceof Box) {
                        shouldStop = true;
                    }
                    //refer to the non-moving entity
                    ((TouchableEntity) entity).onTouchedByEntity(getEntity(), getEntity().getLastMovedDirection());
                    //there was a break here but i removed it because maybe multiple things touch on the same frame
                }

            }

            System.out.println("123 123 123");
            System.out.println(currentTile.getClass());
            if (shouldStop) {
                System.out.println("Should stop");
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                changeState(CartIdle.class);
                //TODO
            } else if (currentTile.changeMovement(getEntity(), getMovement())) {
                System.out.println("ABC");
                //TODO Fix how this makes the cart kind of skip corners and move a lot faster than they should.
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
            } else {
                getEntity().getPosition().translate(SPEED * getMovement().x, SPEED * getMovement().y);
            }

        } else {
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
