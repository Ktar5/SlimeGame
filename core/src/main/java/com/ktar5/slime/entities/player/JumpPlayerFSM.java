package com.ktar5.slime.entities.player;

import com.github.oxo42.stateless4j.delegates.FuncBoolean;
import com.ktar5.slime.engine.fsm.GameStateMachine;
import com.ktar5.slime.engine.fsm.State;
import com.ktar5.slime.entities.player.events.Respawn;
import lombok.Getter;

public class JumpPlayerFSM extends GameStateMachine<JumpPlayerFSM.PlayerState, JumpPlayerFSM.PlayerTrigger> {
    JumpPlayer player;


    public JumpPlayerFSM(JumpPlayer player) {
        super(PlayerState.IDLING);
        this.player = player;
        this.permit(PlayerState.IDLING,
                PlayerState.MOVING);
        this.configure(PlayerState.IDLING).ignore(PlayerTrigger.IDLE);

        this.permit(PlayerState.MOVING,
                PlayerState.IDLING, PlayerState.RESPAWNING);
        this.configure(PlayerState.MOVING).permitReentry(PlayerTrigger.MOVE);

        this.permit(PlayerState.RESPAWNING,
                PlayerState.IDLING);
        this.configure(PlayerState.RESPAWNING).onEntry(player.abilities.get(Respawn.class)::start);
        
        
    }

    @Deprecated
    protected void permitIfAll(PlayerTrigger trigger, FuncBoolean funcBoolean) {
        PlayerState toState = null;
        for (PlayerState state : PlayerState.values()) {
            if (state.getTrigger().equals(trigger)) {
                toState = state;
                break;
            }
        }
        for (PlayerState state : PlayerState.values()) {
            if (state == toState) {
                continue;
            }
            this.configure(state);
            if (this.config.getRepresentation(state).getPermittedTriggers().contains(trigger)) {
                this.config.getRepresentation(state).getPermittedTriggers().remove(trigger);
                this.configure(state).permitIf(trigger, toState, funcBoolean);
            }
        }
    }

    @Override
    public PlayerTrigger getTriggerFrom(PlayerState state) {
        return state.getTrigger();
    }

    @Override
    public void publicFire(PlayerTrigger trigger, Object... args) {
        super.publicFire(trigger, args);
    }

    public enum PlayerTrigger {
        MOVE, IDLE, RESPAWN;
    }

    public enum PlayerState implements State<PlayerTrigger> {
        MOVING(PlayerTrigger.MOVE),
        IDLING(PlayerTrigger.IDLE),
        RESPAWNING(PlayerTrigger.RESPAWN);

        @Getter
        public final PlayerTrigger trigger;

        PlayerState(PlayerTrigger trigger) {
            this.trigger = trigger;
        }
    }

}
