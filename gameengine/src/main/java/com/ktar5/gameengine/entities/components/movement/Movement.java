package com.ktar5.gameengine.entities.components.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.EngConst;
import com.ktar5.gameengine.Feature;
import com.ktar5.gameengine.input.devices.Xbox360Pad;
import com.ktar5.gameengine.util.Updatable;
import com.ktar5.utilities.common.constants.Axis;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class Movement implements Updatable {
    @Getter
    protected Vector2 velocity, velocityDeadzoned, input;
    protected final float speed;
    protected Controller controller;

    @Getter
    private Axis preferred = null;

    public Movement(float speed) {
        this.speed = speed * EngConst.STEP_TIME * EngConst.SCALE;
        //if (Controllers.getControllers().size > 0 && Feature.CONTROLLER.isEnabled())
        //    controller = Controllers.getControllers().get(0);
        velocity = new Vector2();
        velocityDeadzoned = new Vector2();
        input = new Vector2();
    }

    @Override
    public final void update(float dTime) {
        if (Feature.PLAYER_MOVEMENT.isEnabled()) {
            refreshInput();
            calculateMovement(dTime);
            velocityDeadzoned.set(deadzone(velocity.x, .1f), deadzone(velocity.y, .1f));
            velocityDeadzoned.setAngle(velocity.angle());
        } else {
            input.setZero();
            velocity.setZero();
            velocityDeadzoned.setZero();
        }
    }

    public static float deadzone(float value, float zone) {
        if (value >= -zone && value <= zone) {
            return 0;
        }
        return value;
    }

    protected abstract void calculateMovement(float dTime);

    protected void normalizeVelocity() {
        velocity.nor();
        velocity.scl(Math.abs(input.x), Math.abs(input.y));
        velocity.scl(speed);
    }

    private void refreshKeyboardInput() {
        //Do keyboard stuff
        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            input.set(-1, input.y);
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            input.set(1, input.y);
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            input.set(input.x, 1);
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            input.set(input.x, -1);
        }
    }

    private void refreshControllerInput() {
        if (controller != null && Feature.CONTROLLER.isEnabled()) {
            input.set(controller.getAxis(Xbox360Pad.AXIS_LEFT_X), controller.getAxis(Xbox360Pad.AXIS_LEFT_Y));
            input.set(deadzone(.1f, input.x), deadzone(.1f, input.y));
        }
    }

    private void refreshInput() {
        if (Feature.FIRST_PRESSED_MOVEMENT.isEnabled()) {
            if (Math.signum(input.x) == 0 && Math.signum(input.y) != 0) {
                preferred = Axis.Y;
            } else if (Math.signum(input.y) == 0 && Math.signum(input.x) != 0) {
                preferred = Axis.X;
            }
        }
        input.setZero();
        //refreshControllerInput();
        refreshKeyboardInput();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}