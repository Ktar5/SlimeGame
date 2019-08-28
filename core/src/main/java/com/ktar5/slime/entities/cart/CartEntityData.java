package com.ktar5.slime.entities.cart;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.EntityType;
import com.ktar5.slime.world.tiles.base.Rotation;
import lombok.Getter;

@Getter
public class CartEntityData extends EntityData {
    private boolean moving = false;
    private Rotation rotation;

    public CartEntityData(int x, int y, Rotation rotation, boolean moving) {
        super(EntityType.CART, x, y);
        this.moving = moving;
        this.rotation = rotation;
    }

    @Override
    public void processProperty(MapProperties properties) {
    }

    @Override
    public Entity create() {
        return new Cart(this);
    }
}
