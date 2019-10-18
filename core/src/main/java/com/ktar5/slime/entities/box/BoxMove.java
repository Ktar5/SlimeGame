package com.ktar5.slime.entities.box;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LevelData;
import com.ktar5.slime.world.tiles.Air;
import com.ktar5.slime.world.tiles.base.GameTile;
import org.tinylog.Logger;

import java.util.List;

public class BoxMove extends BoxState {
    private static final float SPEED = Settings.BOX_MOVE_SPEED;
    private boolean isBeginning = false;

    protected BoxMove(Box entity) {
        super(entity);
    }

    @Override
    public void start() {
        isBeginning = true;
        if (getEntity().currentMovement == null) {
            end();
            changeState(BoxIdle.class);
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

        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x  * SlimeGame.DPERCENT), (SPEED * getMovement().y  * SlimeGame.DPERCENT));

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

        if (getEntity().isTeleporting() || isBeginning || (pixelsThroughTile < 8 && futurePixelsThroughTile >= 8)) {
            boolean wasBeginning = isBeginning;
            isBeginning = false;
            boolean wasTeleport = getEntity().isTeleporting();
            getEntity().setTeleporting(false);
            GameTile nextTile = levelData.tileFromDirection(currentTileX, currentTileY, getMovement());
            System.out.println(currentTileX + " " + currentTileY);
            GameTile currentTile = levelData.getGameMap()[currentTileX][currentTileY];
            if (nextTile == null) {
                int x = ((int) newPosition.x / 16);
                int y = ((int) newPosition.y / 16);
                Logger.error("Null tile at: " + x + ", " + y + ". Adjusted Y-value: " + (levelData.getGameMap()[0].length - y));
                levelData.getGameMap()[x][y] = nextTile = new Air(x, y);
            }

            if (!wasBeginning && SlimeGame.getGame().getLevelHandler().getCurrentLevel().activateAllTiles(getEntity())) {
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                return;
            }

            List<Entity> entities = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities();
            Vector2 futureAheadHitboxPosition = newPosition.cpy().add(getMovement().x * 10, getMovement().y * 10);
            boolean touchedEntity = false;
            for (Entity entity : entities) {
                if (entity.equals(getEntity())) {
                    continue;
                }
                if (entity instanceof GameEntity && ((GameEntity) entity).isTouching(getEntity().getHitbox(), futureAheadHitboxPosition)) {
                    ((TouchableEntity) entity).onTouchedByEntity(getEntity(), getEntity().getLastMovedDirection());
                    touchedEntity = true;
                    break;
                }
            }
            if (touchedEntity) {
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                changeState(BoxIdle.class);

            } else if (!wasBeginning && !wasTeleport && !currentTile.preMove(getEntity())) {
                //This is only for the teleporter
            } else if (currentTile.changeMovement(getEntity(), getMovement())) {
                //TODO Fix how this makes the player kind of skip corners and move a lot faster than they should.
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                getEntity().getPosition().translate(SPEED * getMovement().x  * SlimeGame.DPERCENT, SPEED * getMovement().y  * SlimeGame.DPERCENT);
            } else if (!nextTile.canCrossThrough(getEntity(), getMovement())) {
                getEntity().getPosition().moveTo((currentTileX * 16) + 8, (currentTileY * 16) + 8);
                //TODO start animation
                System.out.println("Box hit the side and is going idle");
                changeState(BoxIdle.class);
                nextTile.onHitTile(getEntity(), getMovement().opposite());
            } else {
                getEntity().getPosition().translate(SPEED * getMovement().x  * SlimeGame.DPERCENT, SPEED * getMovement().y  * SlimeGame.DPERCENT);
            }

        } else {
            getEntity().getPosition().translate(SPEED * getMovement().x * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
        }
    }


    @Override
    protected void end() {

    }

    public Side getMovement() {
        return getEntity().getLastMovedDirection();
    }
}
