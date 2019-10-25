package com.ktar5.slime.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ktar5.gameengine.Feature;
import com.ktar5.gameengine.camera.CameraBase;
import com.ktar5.gameengine.util.Position;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
public class CameraLookAt extends CameraBase implements Renderable {
    private List<CameraPosition> cameraLocations;
    private Position playerPosition;

    public CameraLookAt(OrthographicCamera camera, Viewport viewport) {
        super(camera, viewport);
    }

    Vector3 camPos = this.getCamera().position;
    float lerp = 0.1f;

    Position cameraPosition;

    boolean testing = true;

    @Override
    public void update(float dTime) {
//        if (Feature.CAMERA_MOVEMENT.isDisabled()) {
//            return;
//        }

//        if (playerPosition == null || cameraLocations == null) {
//            return;
//        }

        if (SlimeGame.getGame().getLevelHandler().getCurrentLevel() == null) {
            return;
        }
        Position position = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getPosition();

        camPos.x += (position.x - camPos.x) * lerp * SlimeGame.DPERCENT;
        camPos.y += (position.y - camPos.y) * lerp * SlimeGame.DPERCENT;
//
//        if (cameraLocations.isEmpty()) {
//            camPos.x += (playerPosition.x - camPos.x) * lerp * SlimeGame.DPERCENT;
//            camPos.y += (playerPosition.y - camPos.y) * lerp * SlimeGame.DPERCENT;
//        } else {
//            float smallestDst2 = Float.MAX_VALUE;
//            CameraPosition smallest = null;
//            for (CameraPosition cameraLocation : cameraLocations) {
//                float currentDst2 = cameraLocation.dst2(playerPosition);
//                if (SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer()
//                        .isTouching(cameraLocation.hitbox, cameraLocation) && currentDst2 < smallestDst2) {
//                    smallestDst2 = currentDst2;
//                    smallest = cameraLocation;
//                }
//            }
//
//            if (smallest != null) {
//                int xHalf = (int) ((playerPosition.x + smallest.x) / 2);
//                int yHalf = (int) ((playerPosition.y + smallest.y) / 2);
//                cameraPosition = new Position(xHalf, yHalf);
//                camPos.x += (xHalf - camPos.x) * lerp * SlimeGame.DPERCENT;
//                camPos.y += (yHalf - camPos.y) * lerp * SlimeGame.DPERCENT;
//            } else {
//                camPos.x += (playerPosition.x - camPos.x) * lerp * SlimeGame.DPERCENT;
//                camPos.y += (playerPosition.y - camPos.y) * lerp * SlimeGame.DPERCENT;
//            }
//
//        }

        if (Feature.PRECISION_CAMERA.isEnabled()) {
            int scale = SlimeGame.getGame().getViewport().getScale();
            float precision = 1f / scale;

            camPos.x = ((int) (camPos.x / precision)) * precision;
            camPos.y = ((int) (camPos.y / precision)) * precision;
        } else {
            camPos.x = (int) camPos.x;
            camPos.y = (int) camPos.y;
        }

        //Set camera position to fixed vector
//        camera.position.set(new Vector3((int) camPos.x, (int) camPos.y, 0));
        camera.position.set(camPos);
        //Update camera
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch, float dTime) {

    }

    public void debug(float dTime) {
        if (cameraLocations == null || cameraLocations.isEmpty()) {
            return;
        } else {
            ShapeRenderer renderer = SlimeGame.getGame().getShapeRenderer();
            for (CameraPosition cameraLocation : cameraLocations) {
                renderer.rect(
                        cameraLocation.x - ((float) cameraLocation.getHitbox().width / 2), cameraLocation.y - ((float) cameraLocation.getHitbox().height / 2),
                        cameraLocation.getHitbox().width, cameraLocation.getHitbox().height);
                renderer.rect(cameraLocation.x, cameraLocation.y, 2, 2);
            }

            if (cameraPosition != null) {
                renderer.setColor(Color.RED);
                renderer.rect(cameraPosition.x, cameraPosition.y, 4, 4);
                renderer.setColor(Color.WHITE);
            }

        }

    }

    @Getter
    public static class CameraPosition extends Position {
        private Rectangle hitbox;

        public CameraPosition(int x, int y, int width, int height) {
            super(x, y);
            hitbox = new Rectangle(width * 16, height * 16);
        }

    }

}
