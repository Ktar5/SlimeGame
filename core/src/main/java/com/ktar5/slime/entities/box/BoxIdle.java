package com.ktar5.slime.entities.box;

public class BoxIdle extends BoxState {

    protected BoxIdle(Box entity) {
        super(entity);
    }

    @Override
    public void start() {
        getEntity().currentMovement = null;
    }

    @Override
    public void onUpdate(float dTime) {

    }

    @Override
    protected void end() {

    }
}
