package com.ktar5.slime.entities;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

@Getter
public abstract class GameEntity<T extends State<T>> extends Entity<T> {
    private Rectangle hitbox;
    private boolean collisionsEnabled = true;

    public GameEntity(float height, float width, Rectangle hitbox) {
        super(height, width);
        this.hitbox = hitbox;
    }

    public boolean isTouching(GameEntity toucher) {
        return toucher.isCollisionsEnabled() && isTouching(toucher.hitbox, toucher.position);
    }

    public boolean isTouching(Rectangle hitbox2, Vector2 position2) {
        return isTouching(hitbox2, (int) position2.x, (int) position2.y);
    }

    //We have to do the adjustments because these calculations assume that the
    // x and y coordinate are at the bottom left of the hitbox
    public boolean isTouching(Rectangle hitbox2, int x2, int y2) {
        float adjustedX1 = position.x - ((float) getHitbox().width / 2f);
        float adjustedY1 = position.y - ((float) getHitbox().height / 2f);

        x2 = (int) (x2 - ((float) hitbox2.width / 2f));
        y2 = (int) (y2 - ((float) hitbox2.height / 2f));


        return collisionsEnabled &&
                adjustedX1 < x2 + hitbox2.width &&
                adjustedX1 + this.hitbox.width > x2 &&
                adjustedY1 < y2 + hitbox2.height &&
                adjustedY1 + this.hitbox.height > y2;
    }

}
