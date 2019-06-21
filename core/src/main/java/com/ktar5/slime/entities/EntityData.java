package com.ktar5.slime.entities;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Position;
import com.ktar5.gameengine.util.PropertyConsumer;

public abstract class EntityData implements PropertyConsumer {
    public final EntityType type;
    public final Position initialPosition;

    public EntityData(EntityType type, int x, int y) {
        this.type = type;
        this.initialPosition = new Position(8 + (x * 16), 8 + (y * 16));
    }

    public abstract Entity create();
}
