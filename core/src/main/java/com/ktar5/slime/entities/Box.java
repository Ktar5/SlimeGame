package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;

public class Box extends Entity {


    public Box() {
        super(1, 1);
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return null;
    }
}
