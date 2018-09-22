package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;

public class Box extends Entity {


    public Box() {
        super(1, 1);
    }

    @Override
    protected SimpleStateMachine initializeStateMachine() {
        return null;
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return null;
    }
}
