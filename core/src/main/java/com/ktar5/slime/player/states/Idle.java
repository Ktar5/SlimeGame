package com.ktar5.slime.player.states;

import com.badlogic.gdx.math.Vector2;

public class Idle extends PlayerState {
    @Override
    public void start() {
    
    }
    
    @Override
    public void update(float dTime) {
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
