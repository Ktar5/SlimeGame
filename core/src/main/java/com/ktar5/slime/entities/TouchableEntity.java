package com.ktar5.slime.entities;

import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;

public interface TouchableEntity {
    public void onTouchedByEntity(Entity entity, Side movement);
}
