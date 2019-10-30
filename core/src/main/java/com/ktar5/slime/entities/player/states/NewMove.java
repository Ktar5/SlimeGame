package com.ktar5.slime.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.cart.Cart;
import com.ktar5.slime.entities.hero.HeroEntity;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LevelData;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.base.GameTile;
import org.tinylog.Logger;

import java.util.List;

public class NewMove extends PlayerState {
    private static final int preMovementFrames = 7;
    private static final float SPEED = Settings.PLAYER_MOVE_SPEED;//3.2f;
//private static final float SPEED = 1.6f;//3.2f;

    //Pixels per frame-- 16 pixels in a tile

    int blocksMoved = 0;
    private int preMovementFrameCount = preMovementFrames;
    private int lastFrameX = 0, lastFrameY = 0;

    boolean isBeginning = false;

    public NewMove(JumpPlayer player) {
        super(player);
    }

    @Override
    public void start() {
        isBeginning = true;
        Vector2 input;
        if (getPlayer().getPreviousNonZeroMovement() != null) {
            input = getPlayer().getPreviousNonZeroMovement();
            getPlayer().setPreviousNonZeroMovement(null);
        } else {
            input = getPlayer().getMovement().getInput();
        }

        if(input.x < 0) input.x = -1;
        else if(input.x > 0) input.x = 1;
        if(input.y < 0) input.y = -1;
        else if(input.y > 0) input.y = 1;

        //If something somehow messed up, let's fix it
        if (input.equals(Vector2.Zero)) {
            System.out.println("FOR SOME REASON THIS IS ZERO");
            end();
            changeState(Idle.class);
        }

        //Make sure we only move in ONE direction (x or y)
        if (input.x != 0)
            input.set(input.x, 0);
        else
            input.set(0, input.y);

        //Reset the last moved direction
        getPlayer().setLastMovedDirection(Side.of((int) input.x, (int) input.y));

        //Reset the # of blocks moved
        blocksMoved = 0;

        //Set animation to beginning of jump animation
        getPlayer().getEntityAnimator().setManualAnimation(EngineManager.get().getAnimationLoader().getAnimation("slime_jump_"
                + getMovement().name().toLowerCase()), 0);
    }

    @Override
    public void onUpdate(float dTime) {
        if (getPlayer().isHaltMovement()) {
            return;
        }

        getPlayer().getMovement().update(dTime);
        if (!getPlayer().getMovement().getInput().equals(Vector2.Zero)) { //if a non-zero input detected
            preMovementFrameCount = preMovementFrames;
            getPlayer().setPreviousNonZeroMovement(getPlayer().getMovement().getInput().cpy());
        } else if (preMovementFrameCount == 0) {
            getPlayer().setPreviousNonZeroMovement(null);
        } else if(SlimeGame.DPERCENT < .9f){
            preMovementFrameCount--;
        }

        final LevelData levelData = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        //Because of the nature of the gameTiles having the bottom left corner of each tile represent the
        //block integer location (tile 1,2 STARTS at the coordinates 1,2)
        int currentTileX = (int) Math.floor(getPlayer().getPosition().x / 16);
        int currentTileY = (int) Math.floor(getPlayer().getPosition().y / 16);

        //Get the position that we WOULD BE MOVING TO IF EVERYTHING GOES WELL so that we can use it
        //as a reference for where we want to go.
        Vector2 newPosition = getPlayer().getPosition().cpy().add((SPEED * getMovement().x * SlimeGame.DPERCENT), (SPEED * getMovement().y * SlimeGame.DPERCENT));

        //Retrieve the number of pixels through the current that the player is, and the number of pixels
        // through the current tile that the player will be if movement continues
        int pixelsThroughTile, futurePixelsThroughTile;
        switch (getMovement()) {
            case UP:
                pixelsThroughTile = (int) (getPlayer().getPosition().y - (currentTileY * 16));
                futurePixelsThroughTile = (int) (newPosition.y - (currentTileY * 16));
                break;
            case DOWN:
                pixelsThroughTile = (int) (16 + (currentTileY * 16) - getPlayer().getPosition().y);
                futurePixelsThroughTile = (int) (16 + (currentTileY * 16) - newPosition.y);
                break;
            case LEFT:
                pixelsThroughTile = (int) (16 + (currentTileX * 16) - getPlayer().getPosition().x);
                futurePixelsThroughTile = (int) (16 + (currentTileX * 16) - newPosition.x);
                break;
            case RIGHT:
                pixelsThroughTile = (int) (getPlayer().getPosition().x - (currentTileX * 16));
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
        if (getPlayer().isEndTeleport() || isBeginning || (pixelsThroughTile < 8 && futurePixelsThroughTile >= 8)) {
            boolean wasBeginning = isBeginning;
            isBeginning = false;
            boolean wasTeleport = getPlayer().isEndTeleport();
            getPlayer().setEndTeleport(false);
            GameTile nextTile = levelData.tileFromDirection(currentTileX, currentTileY, getMovement());
            GameTile currentTile = levelData.getGameMap()[currentTileX][currentTileY];
//            GameTile newGameTile = levelData.getGameMap()[(int) newPosition.x / 16][(int) newPosition.y / 16];

            //If null bug occurs where a tile is null when it shouldn't be.
            if (nextTile == null) {
                int x = ((int) newPosition.x / 16);
                int y = ((int) newPosition.y / 16);
                Logger.error("Null tile at: " + x + ", " + y + ". Adjusted Y-value: " + (levelData.getGameMap()[0].length - y));
                levelData.getGameMap()[x][y] = nextTile = new Air(x, y);
            }

            //Activate all tiles around and below the player once they cross over the center
            if (!wasBeginning && SlimeGame.getGame().getLevelHandler().getCurrentLevel().activateAllTiles(getPlayer())) {
                getPlayer().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                return;
            }

            //NOTE: Other entities such as the Arrow handle the being hit in their own movement class
            List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
            boolean touchedEntity = false;
            Vector2 futureAheadHitboxPosition = getPlayer().getPosition().cpy().add(16 * getMovement().x, 16 * getMovement().y);
            for (Entity entity : entities) {
                if (entity instanceof Box && ((Box) entity).isTouching(getPlayer().getHitbox(), futureAheadHitboxPosition)) {
                    ((TouchableEntity) entity).onTouchedByEntity(getPlayer(), getPlayer().getLastMovedDirection());
                    System.out.println("Touching box");
                    //TODO start the animation
                    touchedEntity = true;
                    break;
                }else if(entity instanceof Cart && ((Cart) entity).isTouching(getPlayer().getHitbox(), futureAheadHitboxPosition)){
                    ((TouchableEntity) entity).onTouchedByEntity(getPlayer(), getPlayer().getLastMovedDirection());
                    System.out.println("Touching cart");
                    touchedEntity = true;
                    break;
                }else if(entity instanceof HeroEntity && ((HeroEntity) entity).isTouching(getPlayer().getHitbox(), futureAheadHitboxPosition)){
                    System.out.println("Touched hero");
                    if(getMovement().opposite().equals(((HeroEntity) entity).facingDirection)){
                        getPlayer().kill("hero");
                    }else{
                        touchedEntity = true;
                        System.out.println("Touched hero");
                    }
                }
            }

            if (touchedEntity) {
                System.out.println("Stopped moving");
                getPlayer().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getPlayer().getEntityState().changeStateAfterUpdate(Idle.class);
                return;
            }

            if (!wasBeginning && !wasTeleport && !currentTile.preMove(getPlayer())) {
                //This is only for the teleporter
            }
            //In case we want to modify where the player is moving without setting them to idle
            //I believe this is only used for the tunnel tile
            else if (currentTile.changeMovement(getPlayer(), getMovement())) {
                //TODO Fix how this makes the player kind of skip corners and move a lot faster than they should.
                getPlayer().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getPlayer().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            }
            //Check for if the tile that the player WOULD BE GOING INTO is able to pass through
            else if (!nextTile.canCrossThrough(getPlayer(), getMovement())) {
                //If it is not air, then that means we have reached a wall
                //This little bit of logic (moving to newX, newY) works because
                //if their next movement would've been to a wall, that means they're
                //right next to a wall, so we just snap them to THE BLOCK
                // AND NOT THE *POSITION* of the new movement
                //
                //Ex: going to hit a wall, so snap them to block before wall
                getPlayer().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                //TODO start animation

                //Change state to idle
                changeState(Idle.class);

                //TODO see above
                getPlayer().getEntityAnimator().setFrame(3);

                nextTile.onHitTile(getPlayer(), getMovement().opposite());
            }
            //This is for regular movement
            else {
                getPlayer().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            }

        } //ELSE

        //If we are just about to enter a new tile
        else if (futurePixelsThroughTile >= 16) {
            //If the player exits the map somehow, send them back to spawn by killing them.
            if (!levelData.isInMapRange((int) newPosition.x / 16, (int) newPosition.y / 16)) {
                getPlayer().kill("exit_map");
                Logger.error("Player exited the map and is being sent back to spawn");
                return;
            }

            //Keeps count of tiles crossed for the animation
            blocksMoved++;
            getPlayer().getEntityAnimator().setFrame(1);

            //This is for regular movement
            getPlayer().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            //In case we want to do something special instead of handle movement
        }
        //This is for regular movement
        else {
            getPlayer().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
        }

    }

    @Override
    protected void end() {

    }

    public Side getMovement() {
        return getPlayer().getLastMovedDirection();
    }
}
