package com.ktar5.slime.entities.ghost;

import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.utilities.common.constants.Axis;
import lombok.Getter;

@Getter
public class Ghost extends Entity<GhostState> implements TouchableEntity {
    private Axis axis;
    private boolean positive;
    private Side movement;

    public Ghost(GhostEntityData data) {
        super(16, 16);
        this.axis = data.axis;
        this.positive = data.positiveDirection;
        this.position.set(data.initialPosition);
        setPositive(positive);
    }

    @Override
    protected SimpleStateMachine<GhostState> initializeStateMachine() {
        return new SimpleStateMachine<>(new GhostMove(this));
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE,
                EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/entity/ghost/ghost_hor.png"),
                16, 16);
    }

    public void setPositive(boolean positive){
        this.positive = positive;
        if(axis.equals(Axis.HORIZONTAL)){
            movement = Side.of(positive ? 1 : -1, 0);
        }else{
            movement = Side.of(0, positive ? 1 : -1);
        }
    }

    @Override
    public void onEntityTouch(Entity entity, Side movement) {
        System.out.println("Ghost touched");
        if (entity.isPlayer()) {
            System.out.println("Entity is player");
            ((JumpPlayer) entity).kill();
        }
    }
}
