package com.ktar5.slime.player.states;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.iConsumer;
import lombok.AllArgsConstructor;

public class Idle extends PlayerState {
    private int timer;
    private IdleAnimations animations;

    public Idle(JumpPlayer player) {
        super(player);
    }

    @Override
    public void start() {
        timer = 0;
        animations = IdleAnimations.values()[0];
    }

    @Override
    public void onUpdate(float dTime) {
        //Handle animations
        if (animations.frames != -1 && animations.frames == timer) {
            timer = 0;
            if (animations.ordinal() != IdleAnimations.values().length - 1) {
                animations = IdleAnimations.values()[animations.ordinal() + 1];
            }
            animations.action.accept(getPlayer());
        } else {
            timer++;
        }

        //Handle previous non zero movement
        //Basically this allows us to go straight into a move if input was detected
        //During the last few frames of movement
        if (getPlayer().getPreviousNonZeroMovement() != null) {
            changeState(Move.class);
            return;
        }

        //Regular idle updating
        getPlayer().getMovement().update(dTime);
        if (!getPlayer().getMovement().getInput().equals(Vector2.Zero)) {
            this.changeState(Move.class);
        }
    }

    @Override
    protected void end() {
        //none
    }

    @AllArgsConstructor
    private enum IdleAnimations {
        BOUNCE_1((player) -> {
            player.getEntityAnimator().setFrame(2);
        }, 6),
        BOUNCE_2((player) -> {
            player.getEntityAnimator().setFrame(3);
        }, 8),
        IDLE_1((player) -> {
            player.getEntityAnimator().setAnimation(
                    EngineManager.get().getAnimationLoader().getAnimation(
                            "slime_idle_" + player.getLastMovedDirection().name().toLowerCase()
                    )
            );
        }, -1);

        private iConsumer<JumpPlayer> action;
        private int frames;
    }

}
