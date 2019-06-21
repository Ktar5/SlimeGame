package com.ktar5.slime.entities.box;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.Rectangle;
import com.ktar5.slime.entities.Teleportable;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.arrow.Arrow;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.entities.player.states.Idle;

public class Box extends GameEntity<BoxState> implements TouchableEntity, Teleportable {
    public Side currentMovement = null;
    private boolean teleporting;

    public Box(BoxEntityData data) {
        super(16, 16, new Rectangle(16, 16));
        this.position.set(data.initialPosition.x, data.initialPosition.y);
    }

    @Override
    protected SimpleStateMachine<BoxState> initializeStateMachine() {
        return new SimpleStateMachine<>(new BoxIdle(this), new BoxMove(this));
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
                EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/entity/box/box1.png"),
                16, 16);
    }

    @Override
    public void onEntityTouch(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            entity.getEntityState().changeStateAfterUpdate(Idle.class);
            if (((JumpPlayer) entity).isSmall()) {
                return;
            }
        }
        if (entity instanceof Arrow) {
            return;
        }
        currentMovement = movement;
        this.getEntityState().changeStateAfterUpdate(BoxMove.class);
    }

    @Override
    public void setTeleporting(boolean teleporting) {
        this.teleporting = teleporting;
    }

    @Override
    public boolean isTeleporting() {
        return teleporting;
    }
}
