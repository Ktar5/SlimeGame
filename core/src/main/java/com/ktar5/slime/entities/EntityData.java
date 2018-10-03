package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Position;
import com.ktar5.slime.engine.util.TiledPropertyConsumer;

public abstract class EntityData implements TiledPropertyConsumer {
    public final EntityType type;
    public final Position initialPosition;

    public EntityData(EntityType type, int x, int y) {
        this.type = type;
        this.initialPosition = new Position(x * 16, y * 16);
    }

    public abstract Entity create();
}
