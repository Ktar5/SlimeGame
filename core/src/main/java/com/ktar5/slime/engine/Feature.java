package com.ktar5.slime.engine;

import org.pmw.tinylog.Logger;

public enum Feature {
    PLAYER_MOVEMENT(true),
    FIRST_PRESSED_MOVEMENT(true),
    
    CAMERA_MOVEMENT(true),
    
    DEBUG(true),
    CONTROLLER(true),

    LOG_MOVEMENT(false),
    LOG_STATE_MACHINE(false),
    
    
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
