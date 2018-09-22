package com.ktar5.slime.player.states;

import com.ktar5.slime.engine.statemachine.State;
import com.ktar5.slime.player.JumpPlayer;
import lombok.Getter;

public abstract class PlayerState extends State<PlayerState> {

//    @Getter(lazy = true)
//    private final JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();

    @Getter
    private final JumpPlayer player;

    public PlayerState(JumpPlayer player) {
        this.player = player;
    }

    @Override
    public void changeState(Class<? extends PlayerState> state) {
        getPlayer().getEntityState().changeStateAfterUpdate(state);
    }

    @Override
    public final void update(float dTime) {
        onUpdate(dTime);
    }


}
