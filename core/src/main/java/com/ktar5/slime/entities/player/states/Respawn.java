package com.ktar5.slime.entities.player.states;

import com.badlogic.gdx.utils.Timer;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;

public class Respawn extends PlayerState {
    public Timer.Task schedule;

    public Respawn(JumpPlayer player) {
        super(player);
    }

    @Override
    public void start() {
        getPlayer().getEntityAnimator().setAnimation(EngineManager.get().getAnimationLoader()
                .getTextureAsAnimation("slime_jump_down"));
        schedule = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                SlimeGame.getGame().getLevelHandler().resetLevel();
            }
        }, 2f);
    }

    @Override
    public void onUpdate(float dTime) {

    }

    @Override
    protected void end() {
        //none
    }

    private Respawn getThis() {
        return this;
    }

    public void cancel() {
        if (schedule != null) {
            schedule.cancel();
        }
    }
}
