package com.ktar5.slime.entities.cart;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.Rectangle;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.arrow.Arrow;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.box.BoxMove;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.entities.player.ShapeState;
import com.ktar5.slime.entities.player.states.Idle;
import com.ktar5.slime.entities.player.states.Respawn;

public class Cart extends GameEntity<CartState> implements TouchableEntity {
    public Side currentMovement = null;

    public Cart(CartEntityData data) {
        super(16, 16, new Rectangle(16, 16));
        this.position.set(data.initialPosition.x, data.initialPosition.y);
    }

    @Override
    protected SimpleStateMachine<CartState> initializeStateMachine() {
        return new SimpleStateMachine<>(new CartIdle(this), new CartMove(this));
    }


    @Override
    public void update(float dTime) {
        super.update(dTime);
        position.set(position.x, position.y);
    }

    @Override
    public void debugRender(ShapeRenderer renderer) {
        renderer.rect(
                position.x - ((float) getHitbox().width / 2), position.y - ((float) getHitbox().height / 2),
                getHitbox().width, getHitbox().height);
        renderer.rect(position.x, position.y, 2, 2);
    }

    //TODO
    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE,
                EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/entity/box/box1.png"),
                16, 16);
    }

    @Override
    public void onTouchedByEntity(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            //If it is stationary, start it's movement
            if (currentMovement == null) {
                if (((JumpPlayer) entity).getShape().equals(ShapeState.TINY)) {
                    entity.getEntityState().changeStateAfterUpdate(Idle.class);
                    return;
                }
                currentMovement = movement;
                this.getEntityState().changeStateAfterUpdate(CartMove.class);
                entity.getEntityState().changeStateAfterUpdate(Idle.class);
            } else if (movement.rotateClockwise(1).equals(currentMovement) || movement.rotateClockwise(1).opposite().equals(currentMovement)) {
                entity.getEntityState().changeStateAfterUpdate(Idle.class);
            } else {
                entity.getEntityState().changeStateAfterUpdate(Respawn.class);
            }
            //TODO same with boxes if two carts hit, hit cart keeps going, initial cart stops
        } else if (entity instanceof Arrow) {
            SlimeGame.getGame().doOnNextFrame(() -> {
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(this);
            });
        } else if (entity instanceof Cart) {
            if (currentMovement == null) {
                currentMovement = movement;
                this.getEntityState().changeStateAfterUpdate(CartMove.class);
                entity.getEntityState().changeStateAfterUpdate(CartIdle.class);
            }
        }
        else if (entity instanceof Box) {
            if (currentMovement == null) {
                currentMovement = movement;
                this.getEntityState().changeStateAfterUpdate(CartMove.class);
                entity.getEntityState().changeStateAfterUpdate(BoxMove.class);
            }
        }
    }

}
