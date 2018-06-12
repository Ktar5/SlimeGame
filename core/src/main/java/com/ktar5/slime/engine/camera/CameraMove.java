package com.ktar5.slime.engine.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ktar5.slime.engine.Feature;

public class CameraMove extends CameraBase {
    private final float speed = 2;
    private final boolean bound = false, round = false;

    public CameraMove(OrthographicCamera camera, Viewport viewport) {
        super(camera, viewport);
    }

    @Override
    public void update(float dTime) {
        if(Feature.CAMERA_MOVEMENT.isDisabled()){
            return;
        }

        float xAxisValue = 0;
        float yAxisValue = 0;

        float temp = speed * dTime;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xAxisValue = -temp;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xAxisValue = temp;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yAxisValue = temp;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yAxisValue = -temp;
        }
        
        if (camera != null) {
            camera.translate(xAxisValue, yAxisValue);
            if (bound) {
                boundCamera();
            }
            if (round) {
                camera.position.set((int) (camera.position.x), (int) (camera.position.y), camera.position.z);
            }
        }
    }

    private void boundCamera() {
/*        Grid grid = SpacebaseManager.reference.grid;
        float camX = camera.position.x;
        float camY = camera.position.y;

        Vector2 camMin = new Vector2(camera.viewportWidth, camera.viewportHeight);
        camMin.scl(camera.zoom / 2); //bring to center and scale by the zoom level
        Vector2 camMax = new Vector2((float) (grid.getWidth() * grid.getScale() * 32),
                (float) (grid.getHeight() * grid.getScale() * 32));
        camMax.sub(camMin); //bring to center

        //keep camera within borders
        camX = Math.min(camMax.x, Math.max(camX, camMin.x));
        camY = Math.min(camMax.y, Math.max(camY, camMin.y));

        camera.position.set(camX, camY, camera.position.z);
        */
    }
}