package com.ktar5.gameengine.entities.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.gameengine.util.Side;
import com.ktar5.gameengine.util.Updatable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class EntityAnimator implements Updatable {
    public boolean halted = false, looping = true, singleFrameMode = false;
    private float lifetimeOfAnimation;
    public Side rotation = Side.DOWN;
    private RenderLayer layer;
    @Setter
    private float unitsX, unitsY;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> animation;

    public EntityAnimator(RenderLayer layer, Animation<TextureRegion> animation, float unitsX, float unitsY) {
        this.layer = layer;
        this.unitsX = unitsX;
        this.unitsY = unitsY;
        this.animation = animation;
    }

//    BitmapFont font = new BitmapFont();
    public void render(SpriteBatch batch, float x, float y, float angle) {
        if (!singleFrameMode) {
            currentFrame = animation.getKeyFrame(lifetimeOfAnimation, looping);
        }
//        font.draw(batch, "ENTITY", (int) (x - (currentFrame.getRegionWidth() / (2))),
//                (int) (y - (currentFrame.getRegionHeight() / (2))));
        batch.draw(currentFrame,
                (int) (x - (currentFrame.getRegionWidth() / (2))),
                (int) (y - (currentFrame.getRegionHeight() / (2))),
                currentFrame.getRegionWidth() / 2, currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),
                unitsX / (currentFrame.getRegionWidth()),
                unitsY / currentFrame.getRegionHeight(),
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

    public void setFrame(TextureRegion texture) {
        singleFrameMode = true;
        this.currentFrame = texture;
    }

    public void setFrame(int frame) {
        singleFrameMode = true;
        if(frame >= getAnimation().getKeyFrames().length){
            return;
        }
        this.currentFrame = getAnimation().getKeyFrames()[frame];
    }

    public void setManualAnimation(Animation<TextureRegion> animation, int frame) {
        this.animation = animation;
        this.currentFrame = animation.getKeyFrames()[frame];
        singleFrameMode = true;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        singleFrameMode = false;
        lifetimeOfAnimation = 0;
    }

    public void resetCurrentAnimation() {
        lifetimeOfAnimation = 0;
    }

    @Override
    public String toString() {
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
