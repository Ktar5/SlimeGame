package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.entities.states.EntityState;
import lombok.Getter;

@Getter
public class Ghost extends Entity<EntityState> {
    private GhostMove direction;
    private boolean positive;

    public Ghost(GhostMove direction, boolean positive) {
        super(1, 1);
        this.direction = direction;
        this.positive = positive;
    }

    @Override
    protected SimpleStateMachine<EntityState> initializeStateMachine() {
        return null;
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return null;
    }

    enum GhostMove {
        HORIZONTAL,
        VERTICAL
    }

}
