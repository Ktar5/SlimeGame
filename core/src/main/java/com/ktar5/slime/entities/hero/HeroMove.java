package com.ktar5.slime.entities.hero;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.cart.Cart;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LevelData;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.base.GameTile;
import org.tinylog.Logger;

import java.util.List;

public class HeroMove extends HeroState {
    private static final float SPEED = Settings.HERO_MOVE_SPEED;
    boolean isBeginning = false;

    public HeroMove(HeroEntity entity) {
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

        //Because of the nature of the gameTiles having the bottom left corner of each tile represent the
        //block integer location (tile 1,2 STARTS at the coordinates 1,2)
        int currentTileX = (int) Math.floor(getEntity().getPosition().x / 16);
        int currentTileY = (int) Math.floor(getEntity().getPosition().y / 16);

        //Get the position that we WOULD BE MOVING TO IF EVERYTHING GOES WELL so that we can use it
        //as a reference for where we want to go.
        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x  * SlimeGame.DPERCENT), (SPEED * getMovement().y * SlimeGame.DPERCENT));

        //Retrieve the number of pixels through the current that the player is, and the number of pixels
        // through the current tile that the player will be if movement continues
        int pixelsThroughTile, futurePixelsThroughTile;
        switch (getMovement()) {
            case UP:
                pixelsThroughTile = (int) (getEntity().getPosition().y - (currentTileY * 16));
                futurePixelsThroughTile = (int) (newPosition.y - (currentTileY * 16));
                break;
            case DOWN:
                pixelsThroughTile = (int) (16 + (currentTileY * 16) - getEntity().getPosition().y);
                futurePixelsThroughTile = (int) (16 + (currentTileY * 16) - newPosition.y);
                break;
            case LEFT:
                pixelsThroughTile = (int) (16 + (currentTileX * 16) - getEntity().getPosition().x);
                futurePixelsThroughTile = (int) (16 + (currentTileX * 16) - newPosition.x);
                break;
            case RIGHT:
                pixelsThroughTile = (int) (getEntity().getPosition().x - (currentTileX * 16));
                futurePixelsThroughTile = (int) (newPosition.x - (currentTileX * 16));
                break;
            default:
                Logger.error(new RuntimeException("Movement is not of up, down, left, or right."));
                return;
        }


//        System.out.println("Cx: " + currentTileX + " Cy:" + currentTileY + " ptt: " + pixelsThroughTile + "Fptt: " + futurePixelsThroughTile);

        //If we are going to be transitioning to a new tile. If we WOULD be moving from the first half of the tile
        //To the second half of the tile, we then need to check whats going on
        //Otherwise we can do other things
        if (getEntity().isTeleporting() || isBeginning || (pixelsThroughTile < 8 && futurePixelsThroughTile >= 8)) {
            boolean wasBeginning = isBeginning;
            isBeginning = false;
            boolean wasTeleport = getEntity().isTeleporting();
            getEntity().setTeleporting(false);
            GameTile nextTile = levelData.tileFromDirection(currentTileX, currentTileY, getMovement());
            GameTile currentTile = levelData.getGameMap()[currentTileX][currentTileY];
            //If null bug occurs where a tile is null when it shouldn't be.
            if (nextTile == null) {
                int x = ((int) newPosition.x / 16);
                int y = ((int) newPosition.y / 16);
                Logger.error("Null tile at: " + x + ", " + y + ". Adjusted Y-value: " + (levelData.getGameMap()[0].length - y));
                levelData.getGameMap()[x][y] = nextTile = new Air(x, y);
            }

            //Activate all tiles around and below the player once they cross over the center
            if (!wasBeginning && SlimeGame.getGame().getLevelHandler().getCurrentLevel().activateAllTiles(getEntity())) {
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                return;
            }

            //NOTE: Other entities such as the Arrow handle the being hit in their own movement class
            List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
            boolean shouldStop = false;
            Vector2 futureAheadHitboxPosition = getEntity().getPosition().cpy().add(16 * getMovement().x, 16 * getMovement().y);
            JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
            if(player.isTouching(getEntity().getHitbox(), futureAheadHitboxPosition)){
                //if we are moving and we run into a player, kill them
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getEntity().setHaltMovement(true);
                player.kill("hero");
            }
            for (Entity entity : entities) {
                if (entity instanceof Box && ((Box) entity).isTouching(getEntity().getHitbox(), futureAheadHitboxPosition)) {
                    ((TouchableEntity) entity).onTouchedByEntity(getEntity(), getEntity().facingDirection);
                    System.out.println("Touching box");
                    //TODO start the animation
                    shouldStop = true;
                    break;
                }else if(entity instanceof Cart && ((Cart) entity).isTouching(getEntity().getHitbox(), futureAheadHitboxPosition)){
                    ((TouchableEntity) entity).onTouchedByEntity(getEntity(), getEntity().facingDirection);
                    System.out.println("Touching cart");
                    shouldStop = true;
                    break;
                }
            }

            if (shouldStop) {
                System.out.println("Stopped moving");
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getEntity().getEntityState().changeStateAfterUpdate(HeroIdle.class);
                return;
            }

            if (!wasBeginning && !wasTeleport && !currentTile.preMove(getEntity())) {
                //This is only for the teleporter
            }
            //In case we want to modify where the player is moving without setting them to idle
            //I believe this is only used for the tunnel tile
            else if (currentTile.changeMovement(getEntity(), getMovement())) {
                //TODO Fix how this makes the player kind of skip corners and move a lot faster than they should.
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getEntity().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            }
            //Check for if the tile that the player WOULD BE GOING INTO is able to pass through
            else if (!nextTile.canCrossThrough(getEntity(), getMovement())) {
                //If it is not air, then that means we have reached a wall
                //This little bit of logic (moving to newX, newY) works because
                //if their next movement would've been to a wall, that means they're
                //right next to a wall, so we just snap them to THE BLOCK
                // AND NOT THE *POSITION* of the new movement
                //
                //Ex: going to hit a wall, so snap them to block before wall
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                //TODO start animation

                //Change state to idle
                changeState(HeroIdle.class);

                //TODO see above
                getEntity().getEntityAnimator().setFrame(3);

                nextTile.onHitTile(getEntity(), getMovement().opposite());
            }
            //This is for regular movement
            else {
                getEntity().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            }

        } //ELSE

        //If we are just about to enter a new tile
        else if (futurePixelsThroughTile >= 16) {
            //If the player exits the map somehow, send them back to spawn by killing them.
            if (!levelData.isInMapRange((int) newPosition.x / 16, (int) newPosition.y / 16)) {
                getEntity().kill();
                Logger.error("Player exited the map and is being sent back to spawn");
                return;
            }

            //TODO animation
//            getPlayer().getEntityAnimator().setFrame(1);

            //This is for regular movement
            getEntity().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            //In case we want to do something special instead of handle movement
        }
        //This is for regular movement
        else {
            getEntity().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
        }
    }

    @Override
    protected void end() {

    }

    //TODO note that facing direction is what well use not lastmoveddirection
    public Side getMovement() {
        return getEntity().facingDirection;
    }
}
