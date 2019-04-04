package com.ktar5.slime.entities.ghost;

import com.ktar5.slime.engine.statemachine.State;
import lombok.Getter;

public abstract class GhostState extends State<GhostState> {
    @Getter
    private final Ghost entity;

    protected GhostState(Ghost entity) {
        this.entity = entity;
    }

    @Override
    public void changeState(Class<? extends GhostState> state) {
        getEntity().getEntityState().changeStateAfterUpdate(state);
    }

}
