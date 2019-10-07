package com.ktar5.slime.hotkeys;

import com.badlogic.gdx.Input;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.input.ControllerInput;
import com.ktar5.gameengine.input.devices.JamPad;
import com.ktar5.slime.KInput;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelFailEvent;

public class RunningHotkeys {
    public static void update() {
        ControllerInput controllerInput = EngineManager.get().getControllerInput();
        if (KInput.isKeyJustPressed(Input.Keys.R) || controllerInput.isButtonJustPressed(JamPad.LEFT_BUMP)
                || controllerInput.isButtonJustPressed(JamPad.RIGHT_BUMP)
                || controllerInput.isButtonJustPressed(JamPad.SELECT)) {
            Analytics.addEvent(new LevelFailEvent(SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer(), "reset"));
            SlimeGame.getGame().getLevelHandler().resetLevel();
        }
        if (KInput.isKeyJustPressed(Input.Keys.TAB)) {
            SlimeGame.getGame().getLevelHandler().toggleDebug();
            EngConst.DEBUG = !EngConst.DEBUG;
        }
    }
}
