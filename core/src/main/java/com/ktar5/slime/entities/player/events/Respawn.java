package com.ktar5.slime.entities.player.events;

import com.badlogic.gdx.utils.Timer;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.entities.player.JumpPlayerFSM;

public class Respawn extends Ability {
    @Override
    public void start() {
        getPlayer().getEntityAnimator().resetAnimation(EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/test/testPlayer.jpg"));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                getPlayer().getPosition().set(SlimeGame.getGame().getLevelHandler().getSpawnX(),
                        SlimeGame.getGame().getLevelHandler().getSpawnY());
                getPlayer().resetAnimation("textures/Player.png", false);
                getPlayer().getPlayerFSM().publicFire(JumpPlayerFSM.PlayerTrigger.IDLE);
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
}
