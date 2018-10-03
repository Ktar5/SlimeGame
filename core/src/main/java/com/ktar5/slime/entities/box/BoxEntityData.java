package com.ktar5.slime.entities.box;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.EntityType;

public class BoxEntityData extends EntityData {
    public BoxEntityData(int x, int y) {
        super(EntityType.BOX, x, y);
    }

    @Override
    public void processProperty(MapProperties properties) {

    }

    @Override
    public Entity create() {
        return new Box(this);
    }
}
