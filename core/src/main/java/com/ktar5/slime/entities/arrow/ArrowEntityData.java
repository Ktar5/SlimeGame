package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.EntityType;

public class ArrowEntityData extends EntityData {
    public final Side movement;

    public ArrowEntityData(int x, int y, Side movement) {
        super(EntityType.ARROW, x, y);
        this.movement = movement;
    }

    @Override
    public void processProperty(MapProperties properties) {

    }

    @Override
    public Entity create() {
        return new Arrow(this);
    }
}
