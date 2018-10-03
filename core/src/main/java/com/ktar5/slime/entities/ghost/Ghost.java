package com.ktar5.slime.entities.ghost;

import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.utilities.common.constants.Axis;
import lombok.Getter;

@Getter
public class Ghost extends Entity<GhostState> implements TouchableEntity {
    private Axis direction;
    private boolean positive;

    public Ghost(GhostEntityData data) {
        super(1, 1);
        this.direction = data.axis;
        this.positive = data.positiveDirection;
        this.position.set(data.initialPosition);
    }

    @Override
    protected SimpleStateMachine<GhostState> initializeStateMachine() {
        return new SimpleStateMachine<>(new GhostMove(this));
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE,
                EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/entity/box/box1.png"),
                1, 1);
    }

    @Override
    public void onEntityTouch(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            ((JumpPlayer) entity).kill();
        }
    }
}
