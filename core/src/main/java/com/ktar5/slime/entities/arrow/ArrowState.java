package com.ktar5.slime.entities.arrow;

import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

public abstract class ArrowState extends State<ArrowState> {

    @Getter
    private final Arrow entity;

    protected ArrowState(Arrow entity) {
        this.entity = entity;
    }

    @Override
    public void changeState(Class<? extends ArrowState> state) {
        getEntity().getEntityState().changeStateAfterUpdate(state);
    }

}
