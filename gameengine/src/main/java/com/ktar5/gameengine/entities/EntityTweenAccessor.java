package com.ktar5.gameengine.entities;

import com.ktar5.gameengine.tweenengine.TweenAccessor;

import java.math.BigDecimal;

public class EntityTweenAccessor implements TweenAccessor<Entity> {

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public int getValues(Entity target, int tweenType, float[] returnValues) {
//        returnValues[0] = round(target.getPosition().x, 1);
//        returnValues[1] = round(target.getPosition().y, 1);
//        return 2;
        returnValues[0] = target.getPosition().x;
        returnValues[1] = target.getPosition().y;
        return 2;

    }

    @Override
    public void setValues(Entity target, int tweenType, float[] newValues) {
//        target.getPosition().set(PixelGridUtil.fixToGrid(newValues[0]),
//                PixelGridUtil.fixToGrid(newValues[1]));
        target.getPosition().set(newValues[0], newValues[1]);

    }
}
