package com.ktar5.slime.entities.hero;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.Rectangle;
import com.ktar5.slime.entities.Teleportable;
import com.ktar5.slime.entities.TouchableEntity;
import com.ktar5.slime.entities.arrow.Arrow;
import com.ktar5.slime.entities.box.Box;
import com.ktar5.slime.entities.box.BoxMove;
import com.ktar5.slime.entities.cart.Cart;
import com.ktar5.slime.entities.player.JumpPlayer;

public class HeroEntity extends GameEntity<HeroState> implements TouchableEntity, Teleportable {
    public Side facingDirection;
    private boolean teleporting;

    public HeroEntity(HeroEntityData data) {
        super(16, 16, new Rectangle(16, 16));
        facingDirection = data.getFacing();
        this.position.set(data.initialPosition.x, data.initialPosition.y);
    }

    @Override
    protected SimpleStateMachine<HeroState> initializeStateMachine() {
        return new SimpleStateMachine<>(new HeroIdle(this), new HeroMove(this), new HeroDying(this));
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

    public void kill() {
        SlimeGame.getGame().doOnNextFrame(() -> {
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(this);
        });
    }

    //if something else touches this
    @Override
    public void onTouchedByEntity(Entity entity, Side movement) {
        if (entity.isPlayer()) {
            if (movement.opposite().equals(facingDirection)) {
                ((JumpPlayer) entity).kill("hero");
            }
        } else if (entity instanceof Arrow) {
            SlimeGame.getGame().doOnNextFrame(() -> {
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(this);
                SlimeGame.getGame().getLevelHandler().getCurrentLevel().getEntities().remove(entity);
            });
        } else if (entity instanceof Cart) {
            kill();
        } else if (entity instanceof Box) {
            entity.getEntityState().changeStateAfterUpdate(BoxMove.class);
        }
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
