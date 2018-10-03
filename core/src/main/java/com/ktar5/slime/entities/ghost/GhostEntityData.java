package com.ktar5.slime.entities.ghost;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.EntityType;
import com.ktar5.utilities.common.constants.Axis;

public class GhostEntityData extends EntityData {
    public Axis axis;
    public boolean positiveDirection = false;

    public GhostEntityData(int x, int y) {
        super(EntityType.GHOST, x, y);
    }

    @Override
    public void processProperty(MapProperties properties) {
        if (properties.containsKey("direction")) {
            axis = Axis.valueOf(properties.get("direction", String.class).toUpperCase());
        } else if (properties.containsKey("positive")) {
            positiveDirection = Boolean.valueOf(properties.get("positive", String.class).toUpperCase());
        }
    }

    @Override
    public Entity create() {
        return new Ghost(this);
    }
}
