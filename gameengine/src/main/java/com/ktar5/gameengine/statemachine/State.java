package com.ktar5.gameengine.statemachine;

import com.ktar5.gameengine.util.Updatable;

public abstract class State<T extends State<T>> implements Updatable {

    public abstract void start();

    public abstract void onUpdate(float dTime);

    protected abstract void end();

    public abstract void changeState(Class<? extends T> state);

    @Override
    public void update(float dTime) {
        onUpdate(dTime);
    }
}