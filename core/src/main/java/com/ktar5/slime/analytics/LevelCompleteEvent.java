package com.ktar5.slime.analytics;

import java.util.UUID;

public class LevelCompleteEvent extends LevelEvent {
    public LevelCompleteEvent(UUID level, String levelName, int levelNum) {
        super(level, levelName, levelNum);
    }

    @Override
    public String getSubEventName() {
        return "complete";
    }
}
