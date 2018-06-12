package com.ktar5.slime.engine.fsm;

public class State0<T> implements State<T> {
    T trigger;

    public State0(T trigger) {
        this.trigger = trigger;
    }

    public void onEnter() {

    }

    public void onExit() {

    }

    @Override
    public T getTrigger() {
        return trigger;
    }
}
