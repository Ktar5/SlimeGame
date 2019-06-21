package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.Rectangle;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.player.JumpPlayer;

public class Arrow extends GameEntity<ArrowState> implements TouchableEntity, Pool.Poolable {
    Side currentMovement;

    //TODO object pooling of arrows
    public Arrow(ArrowEntityData data) {
        super(16, 16,
                (data.movement == Side.LEFT || data.movement == Side.RIGHT) ? new Rectangle(16, 5) :
                        new Rectangle(5, 16));
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().add(this);
        this.position.set(data.initialPosition);
        this.currentMovement = data.movement;
        this.position.setRotation(90 * (data.movement.ofCCOrder()));
        getEntityState().changeStateAfterUpdate(ArrowMove.class);
    }

    @Override
    public void reset() {

    }

    @Override
    protected SimpleStateMachine<ArrowState> initializeStateMachine() {
        return new SimpleStateMachine<>(new ArrowState(this) {
            @Override
            public void start() {

            }

            @Override
            public void onUpdate(float dTime) {

            }

            @Override
            protected void end() {

            }
        }, new ArrowMove(this));
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


    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE,
                EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/entity/arrow/arrow.png"),
                16, 16);
    }

    @Override
    public void onEntityTouch(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            ((JumpPlayer) entity).kill("arrow");
        }
        SlimeGame.getGame().doOnNextFrame(() -> {
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(this);
        });
    }


}
