package com.ktar5.slime.player.states;

import com.badlogic.gdx.utils.Timer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;

public class Respawn extends PlayerState {
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
    
                System.out.println("Testetetetetetetet");
                getThis().changeState(Idle.class);
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
