package com.ktar5.gameengine;

import org.tinylog.Logger;

public enum Feature {
    SINGLE_FRAME(true),

    PLAYER_MOVEMENT(true),
    FIRST_PRESSED_MOVEMENT(true),
    
    CAMERA_MOVEMENT(true),
    
    DEBUG(true),
    CONTROLLER(true),

    LOG_MOVEMENT(true),
    LOG_STATE_MACHINE(true),
    
    
    ;


    private boolean feature;

    private Feature(boolean feature) {
        this.feature = feature;
    }

    public void set(boolean feature) {
        this.feature = feature;
        Logger.debug("Feature: '" + this.name().toUpperCase() + "' was changed to " + feature);
    }

    public boolean isEnabled() {
        return feature;
    }

    public boolean isDisabled() {
        return !feature;
    }

    public void disable() {
        set(false);
    }

    public void enable() {
        set(true);
    }

}
