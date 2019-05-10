package com.ktar5.slime.analytics;

import java.util.UUID;

public class LevelStartEvent extends LevelEvent{
    public LevelStartEvent(UUID level, String levelName, int levelNum) {
        super(level, levelName, levelNum);
    }

    @Override
    public String getSubEventName() {
        return "start";
    }
}
