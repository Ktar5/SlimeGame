package com.ktar5.slime.entities.cart;

import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

public abstract class CartState extends State<CartState> {

    @Getter
    private final Cart entity;

    protected CartState(Cart entity) {
        this.entity = entity;
    }

    @Override
    public void changeState(Class<? extends CartState> state) {
        getEntity().getEntityState().changeStateAfterUpdate(state);
    }

}
