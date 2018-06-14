package com.ktar5.slime.entities.player.states;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.utils.statemachine.State;
import lombok.Getter;

public class PlayerState extends State<PlayerState> {
    @Getter(lazy = true)
    private final JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
    
    @Override
    public void start() {
    
    }
    
    @Override
    public void update(float dTime) {
    
    }
    
    @Override
    protected void end() {
    
    }
    
}
