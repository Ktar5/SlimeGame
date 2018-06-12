package com.ktar5.slime.engine.entities;

import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.util.Identity;
import com.ktar5.slime.engine.util.Position;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public abstract class Entity extends Identity implements Updatable {
    public final Position position;
    private final EntityAnimator entityAnimator;

    public Entity(float height, float width) {
        position = new Position(0, 0);
        this.entityAnimator = initializeRenderer(height, width);
    }

    protected abstract EntityAnimator initializeRenderer(float height, float width);

    @Override
    @CallSuper
    public void update(float dTime) {
        entityAnimator.update(dTime);
    }

    public String getSerialized() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
