package com.ktar5.slime.grid;

public interface Activateable {
    boolean shouldActivate();
    void activate();
    void activeTick();
    void deactivate();

}
