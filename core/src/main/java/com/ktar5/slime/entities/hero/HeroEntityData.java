package com.ktar5.slime.entities.hero;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Side;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.EntityType;
import com.ktar5.slime.world.tiles.base.Rotation;
import lombok.Getter;

@Getter
public class HeroEntityData extends EntityData {
    private Side facing;

    public HeroEntityData(int x, int y, Rotation rotation) {
        super(EntityType.HERO, x, y);
        this.facing = Side.DOWN.rotateClockwise(rotation.ordinal());
    }

    @Override
    public void processProperty(MapProperties properties) {
    }

    @Override
    public Entity create() {
        return new HeroEntity(this);
    }
}
