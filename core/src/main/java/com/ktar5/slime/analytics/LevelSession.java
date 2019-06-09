package com.ktar5.slime.analytics;

import com.ktar5.gameengine.util.Side;

import java.util.UUID;

public class LevelSession {
    public static UUID currentLevelSession = UUID.randomUUID();

    private static StringBuilder builder = new StringBuilder();
    private static int currentFrame = 0;

    public static void newSession() {
        currentLevelSession = UUID.randomUUID();
        builder = new StringBuilder();
        currentFrame = 0;
    }

    public static void addInputOnFrame(Side side) {
        builder.append(currentFrame);
        currentFrame = 0;
        builder.append(side.string);
    }

    public static void incrementFrame() {
        currentFrame += 1;
    }

    public static String serializeInputs() {
        return builder.toString();
    }

}
