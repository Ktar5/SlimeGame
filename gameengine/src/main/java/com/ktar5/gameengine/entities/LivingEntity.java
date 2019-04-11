package com.ktar5.gameengine.entities;

import com.ktar5.gameengine.entities.components.Health;
import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

@Getter
public abstract class LivingEntity<T extends State<T>> extends Entity<T> {
    public final Health health;

    public LivingEntity(int maxHealth, float height, float width) {
        super(height, width);
        this.health = new Health(maxHealth, maxHealth, this);
    }

}
