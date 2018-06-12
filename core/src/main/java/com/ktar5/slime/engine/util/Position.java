package com.ktar5.slime.engine.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.ktar5.utilities.common.constants.Direction;
import lombok.Getter;

@Getter
public class Position extends Vector2 implements Pool.Poolable {
    private float angle = 0;
    private Direction direction;

    public Position(int x, int y) {
        super(x, y);
    }

    public Position(int x, int y, float angle) {
        super(x, y);
        this.angle = angle % 360;
    }

    public Direction getDirection() {
        this.direction = Direction.fromAngleCardinal(this.angle);
        return this.direction;
    }

    public void setRotation(float angle) {
        this.angle = angle % 360;
        this.direction = Direction.fromAngleCardinal(this.angle);
    }

    public void translate(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void fixToGrid() {
        PixelGridUtil.fixToGrid(this);
    }

    @Override
    public void reset() {
        this.x = this.y = 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Position{");
        sb.append("angle=").append(angle);
        sb.append(", direction=").append(direction);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
