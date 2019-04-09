package com.ktar5.slime.engine.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.engine.statemachine.State;
import com.ktar5.slime.engine.util.Identity;
import com.ktar5.slime.engine.util.Position;
import com.ktar5.slime.engine.util.Side;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public abstract class Entity<T extends State<T>> extends Identity implements Updatable {
    private SimpleStateMachine<T> entityState;
    public final Position position;
    private final EntityAnimator entityAnimator;

    @Setter
    private boolean haltMovement = false;
    @Setter
    private Side lastMovedDirection = Side.UP;

    public Entity(float height, float width) {
        position = new Position(0, 0);
        this.entityAnimator = initializeRenderer(height, width);
        this.entityState = initializeStateMachine();
    }

    protected abstract SimpleStateMachine<T> initializeStateMachine();

    protected abstract EntityAnimator initializeRenderer(float height, float width);

    public void debugRender(ShapeRenderer renderer){

    }

    public void reset(){

    }

    @Override
    @CallSuper
    public void update(float dTime) {
        entityAnimator.update(dTime);
        entityState.update(dTime);
    }

    public boolean isPlayer(){
        return false;
    }

    public String getSerialized() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
