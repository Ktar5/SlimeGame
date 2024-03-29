package com.ktar5.slime.entities.box;

import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

public abstract class BoxState extends State<BoxState> {

    @Getter
    private final Box entity;

    protected BoxState(Box entity) {
        this.entity = entity;
    }

    @Override
    public void changeState(Class<? extends BoxState> state) {
        getEntity().getEntityState().changeStateAfterUpdate(state);
    }

}
