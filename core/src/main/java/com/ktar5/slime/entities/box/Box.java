package com.ktar5.slime.entities.box;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;

public class Box extends Entity<BoxState> {


    public Box() {
        super(1, 1);
    }

    @Override
    protected SimpleStateMachine<BoxState> initializeStateMachine() {
        return null;
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return null;
    }

}
