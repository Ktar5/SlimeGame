package com.ktar5.slime.analytics;

import com.ktar5.slime.world.level.LevelData;

public class LevelCompleteEvent extends LevelEvent {
    public LevelCompleteEvent(LevelData data) {
        super(data);
    }

    @Override
    public String getSubEventName() {
        return "complete";
    }
}
