package com.ktar5.slime.player.states;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.statemachine.State;
import lombok.Getter;

public abstract class PlayerState extends State<PlayerState> {
    
    @Getter(lazy = true)
    private final JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();
    
    @Override
    public void changeState(Class<? extends PlayerState> state) {
        getPlayer().getPlayerState().changeStateAfterUpdate(state);
    }
    
    @Override
    public final void update(float dTime) {
        onUpdate(dTime);
    }
    
    
}
