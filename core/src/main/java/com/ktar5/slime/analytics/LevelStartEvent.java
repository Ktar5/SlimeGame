package com.ktar5.slime.analytics;

import com.ktar5.slime.world.level.LevelData;

public class LevelStartEvent extends LevelEvent {
    public LevelStartEvent(LevelData level) {
        super(level);
    }

    @Override
    public String getSubEventName() {
        return "start";
    }
}
