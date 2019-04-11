package com.ktar5.gameengine;

public final class EngConst {
    public static boolean DEBUG = false;

    private EngConst() {
    }

    public static final float MAX_FRAME_TIME = .25f;
    public static final float MAP_PPT = 16f;
    public static final float MAP_SCALE = 1 / MAP_PPT;
    public static final float SCALE = MAP_PPT;
    public static final float STEP_TIME = 1f / 60f;

}
