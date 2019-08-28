package com.ktar5.slime.entities.hero;

import com.badlogic.gdx.utils.Timer;

public class HeroDying extends HeroState {
    public Timer.Task schedule;

    public HeroDying(HeroEntity entity) {
        super(entity);
    }

    @Override
    public void start() {
//        getPlayer().getEntityAnimator().setAnimation(EngineManager.get().getAnimationLoader()
//                .getTextureAsAnimation("slime_jump_down"));
//        schedule = Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                SlimeGame.getGame().getLevelHandler().resetLevel();
//            }
//        }, 2f);
    }

    @Override
    public void onUpdate(float dTime) {

    }

    @Override
    protected void end() {

    }

    public void cancel() {
        if (schedule != null) {
            schedule.cancel();
        }
    }
}
