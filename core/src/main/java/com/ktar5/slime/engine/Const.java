package com.ktar5.slime.engine;

public final class Const {
    public static final boolean DEBUG = true;


    private Const() {
    }

    // MAPS
    public static final float MAP_PPT = 16f;
    public static final float MAP_SCALE = 1 / MAP_PPT;
    public static final int MAP_WALL_LAYER = 0;
    public static final String MAPPATH_TESTING = "maps/generated/sample_map.tmx";

    //GAME
    public static final float SCALE = MAP_PPT;
    public static final float CAMERA_SMOOTH = 0.075f;

    // PHYSICS
    public static final int H_GRAV = 0;
    public static final int V_GRAV = 0;
    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    public static final float MAX_FRAME_TIME = 0.25f;

    // FIXTURES
    public static final float STD_FIXTURE_DENSITY = 0.5f;
    public static final float STD_FIXTURE_FRICTION = 0.4f;
    public static final float STD_FICTURE_RESTITUTION = 0.25f;

    // ENTITY

    //PLAYER
    public static final int PLAYER_STARTING_HEALTH = 5;
    public static final float PLAYER_MAX_VEL = 1.5f * SCALE;
    public static final float PLAYER_DECEL_FACTOR = 10f;
    public static final float PLAYER_ACCEL_FACTOR = 10f + PLAYER_DECEL_FACTOR;


}
