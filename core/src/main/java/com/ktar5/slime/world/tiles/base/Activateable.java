package com.ktar5.slime.world.tiles.base;

public interface Activateable {
    boolean shouldActivate();

    void activate();

    void activeTick();

    void deactivate();

}
