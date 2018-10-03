package com.ktar5.slime.entities;

import com.ktar5.slime.engine.entities.Entity;
import com.ktar5.slime.engine.util.Side;

public interface TouchableEntity {
    public void onEntityTouch(Entity entity, Side movement);
}
