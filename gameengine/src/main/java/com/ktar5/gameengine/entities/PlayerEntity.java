package com.ktar5.gameengine.entities;

import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.entities.components.movement.Movement;
import com.ktar5.gameengine.entities.components.movement.SetVelocityMovement;
import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

@Getter
public abstract class PlayerEntity<T extends State<T>> extends LivingEntity<T> {
    protected Movement movement = new SetVelocityMovement(40f);
    
    public PlayerEntity(int maxHealth, float height, float width) {
        super(maxHealth, height, width);
    }
    
    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE,
                EngineManager.get().getAnimationLoader().getAnimation(getDefaultAnimation()),
                width, height);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    protected abstract String getDefaultAnimation();
    
}
