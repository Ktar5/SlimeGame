package com.ktar5.gameengine.input;

import com.badlogic.gdx.InputProcessor;

public class KInput implements InputProcessor {
    private static boolean[] KEYS_DOWN_FRAME = new boolean[256];
    private static boolean keyPressed = false;

    public static void update() {
        for (int i = 0; i < KEYS_DOWN_FRAME.length; i++) {
            KEYS_DOWN_FRAME[i] = false;
        }
        keyPressed = false;
    }

    public static boolean isKeyJustPressed(int key) {
        if(key == -1){
            return keyPressed;
        }
        return KEYS_DOWN_FRAME[key];
    }

    public static boolean isKeyJustPressed(int... keys){
        for (int i : keys) {
            if(isKeyJustPressed(i)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("PRESSING KEY: " + keycode);
        keyPressed = true;
        KEYS_DOWN_FRAME[keycode] = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
