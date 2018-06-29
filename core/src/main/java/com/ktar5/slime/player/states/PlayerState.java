package com.ktar5.slime.player.states;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.utils.statemachine.State;
import lombok.Getter;
import org.pmw.tinylog.Logger;

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
    
    @Override
    public void changeState(Class<? extends PlayerState> state) {
        Logger.debug("Changing state from " + this.getClass().getSimpleName() + " to " + state.getSimpleName()
                + " @ " + System.currentTimeMillis());
        getPlayer().getPlayerState().changeState(state);
    }
    
}
