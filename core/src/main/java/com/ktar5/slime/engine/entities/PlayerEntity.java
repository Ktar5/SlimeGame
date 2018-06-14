package com.ktar5.slime.engine.entities;

import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.entities.components.movement.Movement;
import com.ktar5.slime.engine.entities.components.movement.SetVelocityMovement;
import lombok.Getter;

@Getter
public abstract class PlayerEntity extends LivingEntity {
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
    
    protected abstract String getDefaultAnimation();
    
}
