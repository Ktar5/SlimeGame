package com.ktar5.slime.entities.states;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.statemachine.State;
import lombok.Getter;

public abstract class EntityState extends State<EntityState> {

    @Getter
    private final Entity<EntityState> entity;

    protected EntityState(Entity<EntityState> entity) {
        this.entity = entity;
    }

    @Override
    public void changeState(Class<? extends EntityState> state) {
        getEntity().getEntityState().changeStateAfterUpdate(state);
    }

    @Override
    public final void update(float dTime) {
        onUpdate(dTime);
    }


}
