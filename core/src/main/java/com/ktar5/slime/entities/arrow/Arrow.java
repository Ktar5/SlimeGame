package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.player.JumpPlayer;

public class Arrow extends Entity<ArrowState> implements TouchableEntity {
    public Side currentMovement;
    int lastX = 0;
    int lastY = 0;


    //TODO object pooling of arrows
    public Arrow(ArrowEntityData data) {
        super(16, 16);
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().add(this);
        this.position.set(data.initialPosition);
        this.currentMovement = data.movement;
        getEntityState().changeStateAfterUpdate(ArrowMove.class);
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
        if (!isHaltMovement()) {
            if (lastX == (int) position.x / 16 && lastY == (int) position.y / 16) {
                return;
            }
            lastY = (int) position.y / 16;
            lastX = (int) position.x / 16;
//            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid().activateAllTiles(this);
        }
    }

    @Override
    public void debugRender(ShapeRenderer renderer) {
        EngineManager.get().getRenderManager().getShapeRenderer().circle(position.x, position.y, 5);
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
            ((JumpPlayer) entity).kill();
        }
        EngineManager.get().getRenderManager().doOnNextFrame(() -> {
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(this);
        });
    }
}
