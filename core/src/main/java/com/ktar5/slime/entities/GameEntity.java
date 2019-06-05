package com.ktar5.slime.entities;

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
        return this.position.x < toucher.position.x + toucher.hitbox.width &&
                this.position.x + this.hitbox.width > toucher.position.x &&
                this.position.y < toucher.position.y + toucher.hitbox.height &&
                this.position.y + this.hitbox.height > toucher.position.y;
    }

}
