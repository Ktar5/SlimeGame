package com.ktar5.slime.hotkeys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ktar5.gameengine.input.KInput;
import com.ktar5.slime.SlimeGame;

public class GeneralHotkeys {

    public static void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
            if (KInput.isKeyJustPressed(Input.Keys.ENTER)) {
                SlimeGame.getGame().getData().fullscreen = !SlimeGame.getGame().getData().fullscreen;
                if (SlimeGame.getGame().getData().fullscreen) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else {
                    Gdx.graphics.setWindowedMode(960, 540);
                }
            }
        }

    }
}
