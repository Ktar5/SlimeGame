package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;

public class Monster extends Entity {
    public Monster(float height, float width) {
        super(height, width);
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return null;
    }
}
