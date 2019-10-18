package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Settings;
import com.ktar5.slime.world.level.LoadedLevel;
import com.ktar5.slime.world.tiles.base.GameTile;
import org.tinylog.Logger;

import java.util.List;

public class ArrowMove extends ArrowState {
    private static final float SPEED = Settings.ARROW_MOVE_SPEED;

    protected ArrowMove(Arrow entity) {
        super(entity);
    }

    @Override
    public void start() {
        if (getEntity().currentMovement == null) {
            end();
            Logger.debug("broken arrows!!!!!!!!!!!!");
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
        final LoadedLevel levelData = SlimeGame.getGame().getLevelHandler().getCurrentLevel();

        Vector2 newPosition = getEntity().getPosition().cpy().add((SPEED * getMovement().x * SlimeGame.DPERCENT), (SPEED * getMovement().y  * SlimeGame.DPERCENT));

        boolean touchedEntity = false;

        JumpPlayer player = levelData.getPlayer();
        if (player.isTouching(getEntity())) {
            player.kill("arrow");
            touchedEntity = true;
        }

        List<Entity> entities = levelData.getEntities();
//        Vector2 futureAheadHitboxPosition = newPosition.cpy().add(getMovement().x * 10, getMovement().y * 10);
        for (Entity entity : entities) {
            if (entity instanceof Arrow) {
                continue;
            }
            if (entity instanceof GameEntity && ((GameEntity) entity).isTouching(getEntity())) {
                ((TouchableEntity) entity).onTouchedByEntity(getEntity(), getEntity().getLastMovedDirection());
                touchedEntity = true;
                break;
            }
        }


        GameTile newGameTile = levelData.getGameMap()[(int) (newPosition.x / 16)][(int) (newPosition.y / 16)];

        if (touchedEntity) {
            System.out.println("touched entity");
            killArrow();
        } else if (!newGameTile.canCrossThrough(getEntity(), getMovement())) {
            getEntity().getPosition().translate(SPEED * getMovement().x  * SlimeGame.DPERCENT, SPEED * getMovement().y * SlimeGame.DPERCENT);
            killArrow();
            //TODO start an animation instead of moving it further
        } else {
            getEntity().getPosition().translate(SPEED * getMovement().x  * SlimeGame.DPERCENT, SPEED * getMovement().y  * SlimeGame.DPERCENT);
        }
    }


    @Override
    protected void end() {

    }

    public void killArrow() {
        getEntity().removeArrow();
    }

    public Side getMovement() {
        return getEntity().currentMovement;
    }
}
