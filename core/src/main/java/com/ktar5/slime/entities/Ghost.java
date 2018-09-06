package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import lombok.Getter;

@Getter
public class Ghost extends Entity {
    private GhostMove direction;
    private boolean positive;

    public Ghost(float height, float width, GhostMove direction, boolean positive) {
        super(height, width);
        this.direction = direction;
        this.positive = positive;
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
