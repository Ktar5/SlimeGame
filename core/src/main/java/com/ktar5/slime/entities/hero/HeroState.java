package com.ktar5.slime.entities.hero;

import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

public abstract class HeroState extends State<HeroState> {

    @Getter
    private final HeroEntity entity;

    public HeroState(HeroEntity entity) {
        this.entity = entity;
    }

    @Override
    public void changeState(Class<? extends HeroState> state) {
        getEntity().getEntityState().changeStateAfterUpdate(state);
    }

}
