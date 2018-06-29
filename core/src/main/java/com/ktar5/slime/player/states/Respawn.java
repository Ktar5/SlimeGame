package com.ktar5.slime.player.states;

import com.badlogic.gdx.utils.Timer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;

public class Respawn extends PlayerState {
    @Override
    public void start() {
        getPlayer().getEntityAnimator().resetAnimation(EngineManager.get().getAnimationLoader()
                .getTextureAsAnimation("textures/test/testPlayer.jpg"));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                getPlayer().getPosition().set(SlimeGame.getGame().getLevelHandler().getSpawnX(),
                        SlimeGame.getGame().getLevelHandler().getSpawnY());
                getPlayer().resetAnimation("textures/Player.png", false);
                
                getThis().changeState(Idle.class);
            }
        }, 2f);
    }
    
    @Override
    public void update(float dTime) {
        //none
    }
    
    @Override
    protected void end() {
        //none
    }
    
    private Respawn getThis() {
        return this;
    }
}
