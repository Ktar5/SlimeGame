package com.ktar5.slime.entities.ghost;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.EntityType;
import com.ktar5.slime.world.tiles.base.Rotation;
import com.ktar5.utilities.common.constants.Axis;

public class GhostEntityData extends EntityData {
    public Axis axis;
    public boolean positiveDirection = false;

    public GhostEntityData(int x, int y, Rotation rotation) {
        super(EntityType.GHOST, x, y);
        if(rotation.equals(Rotation.DEG_0) || rotation.equals(Rotation.DEG_180)){
            axis = Axis.HORIZONTAL;
        }else{
            axis = Axis.VERTICAL;
        }
    }

    @Override
    public void processProperty(MapProperties properties) {
        if (properties.containsKey("positive")) {
            positiveDirection = Boolean.valueOf(properties.get("positive", String.class).toUpperCase());
        }
    }

    @Override
    public Entity create() {
        return new Ghost(this);
    }
}
