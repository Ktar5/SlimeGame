package com.ktar5.slime.entities;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.statemachine.State;
import lombok.Getter;

@Getter
public abstract class GameEntity<T extends State<T>> extends Entity<T> {
    private Rectangle hitbox;

    public GameEntity(float height, float width, Rectangle hitbox) {
        super(height, width);
        this.hitbox = hitbox;
    }

//    public abstract void onEntityTouch(Entity entity, Side movement);

    public boolean isTouching(GameEntity toucher) {
//        float adjustedX1 = position.x - ((float) getHitbox().width / 2);
//        float adjustedY1 = position.y - ((float) getHitbox().height / 2);
//
//        float adjustedX2 = toucher.position.x - ((float) toucher.getHitbox().width / 2);
//        float adjustedY2 = toucher.position.y - ((float) toucher.getHitbox().height / 2);
//
//
//        return adjustedX1 < adjustedX2 + toucher.hitbox.width &&
//                adjustedX1 + this.hitbox.width > toucher.position.x &&
//                adjustedY1 < adjustedY2 + toucher.hitbox.height &&
//                adjustedY1 + this.hitbox.height > toucher.position.y;
        return isTouching(toucher.hitbox, toucher.position);
    }

    public boolean isTouching(Rectangle hitbox2, Vector2 position2) {
        return this.position.x < position2.x + hitbox2.width &&
                this.position.x + this.hitbox.width > position2.x &&
                this.position.y < position2.y + hitbox2.height &&
                this.position.y + this.hitbox.height > position2.y;
    }

}
