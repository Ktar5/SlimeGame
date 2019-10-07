package com.ktar5.gameengine.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.ktar5.gameengine.Feature;

public class ControllerInput {

    boolean[] buttonsJustPressed = new boolean[15];
    protected Controller controller;

    public boolean isButtonJustPressed(int buttonCode){
        return buttonsJustPressed[buttonCode];
    }

    public boolean isButtonJustPressed(int... buttonCode){
        for (int i : buttonCode) {
            if(isButtonJustPressed(i)){
                return true;
            }
        }
        return false;
    }

    public void refreshControllers() {
        if (controller == null && Controllers.getControllers().size > 0 && Feature.CONTROLLER.isEnabled()) {
            controller = Controllers.getControllers().get(0);
        }
        Controllers.addListener(new ControllerAdapter() {
            @Override
            public void connected(Controller newController) {
                controller = Controllers.getControllers().get(0);
            }

            @Override
            public void disconnected(Controller controller) {

            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                System.out.println("Controller button pressed: " + buttonCode);
                buttonsJustPressed[buttonCode] = true;
                return true;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                System.out.println("POV MOVED " + povCode + " povdir " + value.name());
                return true;
            }
        });
    }

    public void update() {
        if (controller != null) {
            for (int i = 0; i < 15; i++) {
                buttonsJustPressed[i] = false;
            }
        }
    }

}
