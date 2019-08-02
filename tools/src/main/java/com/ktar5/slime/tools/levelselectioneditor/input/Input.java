package com.ktar5.slime.tools.levelselectioneditor.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.ZoomablePannableWidget;

public class Input implements InputProcessor {

    public Input() {

    }

    public boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean keyDown(int keycode) {
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
        Actor hit = Main.getInstance().mainScene.hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), false);
        if (hit instanceof ZoomablePannableWidget) {
            ((ZoomablePannableWidget) hit).scale(amount);
        }
        return true;
    }
}

