package com.ktar5.gameengine.rendering;

public interface CustomizedRender {
    CustomizedRender BLANK = new CustomizedRender() {
        @Override
        public void preRender(float dTime) { }
        @Override
        public void postRender(float dTime) { }
        @Override
        public void preDebug(float dTime) { }
        @Override
        public void postDebug(float dTime) { }
    };

    default void preRender(float dTime){

    }

    default void postRender(float dTime){

    }

    default void preDebug(float dTime){

    }

    default void postDebug(float dTime){

    }
}
