package com.ktar5.slime.player;

import com.ktar5.slime.engine.tweenengine.TweenAccessor;

public class JumpPlayerTweenAccessor implements TweenAccessor<JumpPlayer> {
    
    @Override
    public int getValues(JumpPlayer target, int tweenType, float[] returnValues) {
        returnValues[0] = target.getPosition().x;
        returnValues[1] = target.getPosition().y;
        return 2;
    }
    
    @Override
    public void setValues(JumpPlayer target, int tweenType, float[] newValues) {
        target.getPosition().set(newValues[0], newValues[1]);
    }
}
