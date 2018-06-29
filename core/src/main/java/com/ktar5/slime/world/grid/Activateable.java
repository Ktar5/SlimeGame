package com.ktar5.slime.world.grid;

public interface Activateable {
    boolean shouldActivate();
    void activate();
    void activeTick();
    void deactivate();

}
