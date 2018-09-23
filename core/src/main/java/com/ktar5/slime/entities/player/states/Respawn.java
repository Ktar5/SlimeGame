package com.ktar5.slime.entities.player.states;

import com.badlogic.gdx.utils.Timer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.entities.player.JumpPlayer;

public class Respawn extends PlayerState {
    public Respawn(JumpPlayer player) {
        super(player);
    }

    @Override
    public void start() {
        getPlayer().getEntityAnimator().setAnimation(EngineManager.get().getAnimationLoader()
                .getTextureAsAnimation("slime_jump_down"));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                getPlayer().getPosition().set(SlimeGame.getGame().getLevelHandler().getSpawnX(),
                        SlimeGame.getGame().getLevelHandler().getSpawnY());
                getPlayer().resetAnimation("slime_jump_down");

                getThis().changeState(Idle.class);
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
}
