package com.ktar5.slime.tools.levelselectioneditor.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class InputHandler extends InputListener {
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return super.touchDown(event, x, y, pointer, button);
    }

    /*
            if (path != null) {
                int xFixed = (int) ((x - renderer.getRenderX()) / renderer.getScale());
                int yFixed = (int) ((y - renderer.getRenderY()) / renderer.getScale());

                System.out.println("Fixed: " + xFixed + " , " + yFixed);

                //TODO
    //                    PathPoint point = new PathPoint(, xFixed, yFixed);
    //                    path.addPoint(point);
            }
     */
    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        return super.mouseMoved(event, x, y);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return super.keyDown(event, keycode);
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        return super.keyTyped(event, character);
    }
}
