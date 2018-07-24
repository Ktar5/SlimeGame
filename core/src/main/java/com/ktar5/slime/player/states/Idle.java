package com.ktar5.slime.player.states;

import com.badlogic.gdx.math.Vector2;

public class Idle extends PlayerState {
    int timeUntilState2;
    int timeUntilReset;
    
    @Override
    public void start() {
        timeUntilState2 = 6;
        timeUntilReset = 14;
    }
    
    @Override
    public void onUpdate(float dTime) {
        getPlayer().getEntityAnimator().setFrame(2);
        if (timeUntilReset < 0) {
            getPlayer().getEntityAnimator().setFrame(0);
        } else if (timeUntilState2 < 0) {
            getPlayer().getEntityAnimator().setFrame(3);
        }else{
            getPlayer().getEntityAnimator().setFrame(2);
        }
        
        timeUntilReset--;
        timeUntilState2--;
        
        
        getPlayer().getMovement().update(dTime);
        if (!getPlayer().getMovement().getInput().equals(Vector2.Zero)) {
            System.out.println(getPlayer().getMovement().getInput());
            this.changeState(Move.class);
        }
    }
    
    @Override
    protected void end() {
        //none
    }
}
