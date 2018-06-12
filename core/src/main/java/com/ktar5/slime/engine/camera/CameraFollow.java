package com.ktar5.slime.engine.camera;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ktar5.slime.engine.Feature;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.util.Position;
import lombok.Setter;

@Setter
public class CameraFollow extends CameraBase implements Renderable {
    private Position position;
    
    public CameraFollow(OrthographicCamera camera, Viewport viewport, Position position) {
        super(camera, viewport);
        this.position = position;
    }
    
    public void updatePosition(Position position){
        this.position = position;
    }
    
    @Override
    public void update(float dTime) {
        if (Feature.CAMERA_MOVEMENT.isDisabled()) {
            return;
        }
        
        if(position == null){
            return;
        }
        
        //Set camera position to fixed vector
        camera.position.set(new Vector3(position, 0));
        //Update camera
        camera.update();
    }
    
    public Vector3 boundToPlayer(Vector3 camPos, Vector2 playPos, float xDistance, float yDistance) {
        Vector2 v1 = playPos.cpy().add(xDistance, yDistance);
        Vector2 v2 = playPos.cpy().sub(xDistance, yDistance);
        if (camPos.x > v1.x) {
            camPos.x = v1.x;
        } else if (camPos.x < v2.x) {
            camPos.x = v2.x;
        }
        if (camPos.y > v1.y) {
            camPos.y = v1.y;
        } else if (camPos.y < v2.y) {
            camPos.y = v2.y;
        }
        return camPos;
    }
    
    public boolean withinBounds(Vector3 camPos, Vector2 playPos, float xDistance, float yDistance) {
        Vector2 v1 = playPos.cpy().add(xDistance, yDistance);
        Vector2 v2 = playPos.cpy().sub(xDistance, yDistance);
        return (camPos.x < v1.x && camPos.x > v2.x && camPos.y < v1.y && camPos.y > v2.y);
    }
    
    @Override
    public void render(SpriteBatch batch, float dTime) {
    
    }
    
    public void debug(float dTime) {
        ;
        Renderable.drawSquare(camera.position.x, camera.position.y, .5f, .5f, Color.CYAN);
    }
    
}
