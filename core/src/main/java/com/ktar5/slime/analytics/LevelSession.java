package com.ktar5.slime.analytics;

import java.util.UUID;

public class LevelSession {
    public static UUID currentLevelSession = UUID.randomUUID();

    public static void newSession(){
        currentLevelSession = UUID.randomUUID();
    }
}
