package com.ktar5.slime.engine.entities.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.slime.engine.util.Updatable;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class EntityAnimator implements Updatable {
    public boolean halted = false, looping = true;
    private float lifetimeOfAnimation;
    private RenderLayer layer;
    private float unitsX, unitsY;
    private Animation<TextureRegion> animation;

    public EntityAnimator(RenderLayer layer, Animation<TextureRegion> animation, float unitsX, float unitsY) {
        this.layer = layer;
        this.unitsX = unitsX;
        this.unitsY = unitsY;
        this.animation = animation;
    }

    public void render(SpriteBatch batch, float x, float y, float angle) {
        TextureRegion frame = this.getFrame();
        batch.draw(frame,
                x - (frame.getRegionWidth() / (2)),
                y - (frame.getRegionHeight() / (2)),
                frame.getRegionWidth() / 2, frame.getRegionHeight() / 2,
                frame.getRegionWidth(), frame.getRegionHeight(),
                unitsX / (frame.getRegionWidth()),
                unitsY / frame.getRegionHeight(),
                angle);
    }

    @Override
    public void update(float dTime) {
        if (halted) {
            return;
        }
        //Keep track of the time that has passed
        lifetimeOfAnimation += dTime;
    }

    public void resetAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        lifetimeOfAnimation = 0;
    }

    public void resetAnimation() {
        lifetimeOfAnimation = 0;
    }

    public TextureRegion getFrame() {
        return animation.getKeyFrame(lifetimeOfAnimation, looping);
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public enum RenderLayer {
        BACKGROUND,

        FURTHEST,
        FAR,
        MIDDLE,
        NEAR,
        NEAREST,

        OVERLAY;
    }

}
