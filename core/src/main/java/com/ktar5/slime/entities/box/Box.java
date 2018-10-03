package com.ktar5.slime.entities.box;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.TouchableEntity;

public class Box extends Entity<BoxState> implements TouchableEntity {
    public Side currentMovement = null;
    int lastX = 0;
    int lastY = 0;

    public Box(BoxEntityData data) {
        super(16, 16);
        this.position.set(data.initialPosition);
    }

    @Override
    protected SimpleStateMachine<BoxState> initializeStateMachine() {
        return new SimpleStateMachine<>(new BoxIdle(this), new BoxMove(this));
    }

    @Override
    public void update(float dTime){
        super.update(dTime);
        position.set(position.x, position.y);
        if (!isHaltMovement()) {
            if (lastX == (int) position.x / 16 && lastY == (int) position.y / 16) {
                return;
            }
            lastY = (int) position.y / 16;
            lastX = (int) position.x / 16;
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid().activateAllTiles(this);
        }
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE,
                EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/entity/box/box1.png"),
                16, 16);
    }

    @Override
    public void onEntityTouch(Entity entity, Side movement) {
        currentMovement = movement;
        this.getEntityState().changeStateAfterUpdate(BoxMove.class);
    }
}
