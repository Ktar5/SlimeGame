package com.ktar5.slime.entities.player.states;

import com.ktar5.gameengine.statemachine.State;
import com.ktar5.slime.entities.player.JumpPlayer;
import lombok.Getter;

public abstract class PlayerState extends State<PlayerState> {

    @Getter
    private final JumpPlayer player;

    public PlayerState(JumpPlayer player) {
        this.player = player;
    }

    @Override
    public void changeState(Class<? extends PlayerState> state) {
        getPlayer().getEntityState().changeStateAfterUpdate(state);
    }

}
