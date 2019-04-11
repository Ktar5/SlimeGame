package com.ktar5.slime.entities.monster;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;

public class Monster extends Entity {
    public Monster(float height, float width) {
        super(height, width);
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
