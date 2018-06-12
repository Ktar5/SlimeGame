package com.ktar5.slime.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ktar5.slime.engine.Const;
import com.ktar5.slime.engine.core.EngineManager;

public class PixelGridUtil {
    public static float fixToGrid(float number) {
        return ((int) (number * Const.SCALE) / Const.SCALE);
    }

    public static Vector2 fixToGrid(Vector2 vec) {
        vec.set((int) (vec.x * Const.SCALE) / Const.SCALE, (int) (vec.y * Const.SCALE) / Const.SCALE);
        return vec;
    }

    public static Vector3 fixToGrid(Vector3 vec) {
        vec.set((int) (vec.x * Const.SCALE) / Const.SCALE,
                (int) (vec.y * Const.SCALE) / Const.SCALE,
                (int) (vec.z * Const.SCALE) / Const.SCALE);
        return vec;
    }

    public static FitViewport getViewport(){
        return ((FitViewport) EngineManager.get().getCameraBase().getViewport());
    }

    public void test(){

    }

    public static Vector2 fixToResolution(Vector2 vec) {
        vec.set((int) (vec.x * (getViewport().getScreenWidth() / Gdx.graphics.getWidth())) / Const.SCALE, (int) (vec.y * Const.SCALE) / Const.SCALE);
        return vec;
    }

    public static Vector3 fixToResolution(Vector3 vec) {
        vec.set((int) (vec.x * (getViewport().getScreenWidth() / Gdx.graphics.getWidth())) / (getViewport().getScreenWidth() / Gdx.graphics.getWidth()),
                (int) (vec.y * (getViewport().getScreenHeight() / Gdx.graphics.getHeight())) / (getViewport().getScreenHeight() / Gdx.graphics.getHeight()),
                (int) (vec.z * Const.SCALE) / Const.SCALE);
        return vec;
    }

}
