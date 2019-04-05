package com.ktar5.slime.engine;

public final class EngConst {
    public static final float MAX_FRAME_TIME = .25f;
    public static boolean DEBUG = false;


    private EngConst() {
    }

    // MAPS
    public static final float MAP_PPT = 16f;
    public static final float MAP_SCALE = 1 / MAP_PPT;

    //GAME
    public static final float SCALE = MAP_PPT;


    public static final float STEP_TIME = 1f / 60f;


}
