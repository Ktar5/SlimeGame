package com.ktar5.slime.utils.statemachine;

import com.ktar5.slime.engine.util.Updatable;

public abstract class State<T extends State<T>> implements Updatable {
    public abstract void start();
    
    public abstract void update(float dTime);
    
    protected abstract void end();
    
    public abstract void changeState(Class<? extends T> state);
    
    
}