package com.ktar5.slime.analytics;

import com.ktar5.slime.world.level.LevelData;
import org.tinylog.Logger;

public class LevelStartEvent extends LevelEvent {
    public LevelStartEvent(LevelData level) {
        super(level);
        LevelSession.newSession();
        Logger.debug("Started level: ID: " + level.getUuid().toString() +"  Number: " + level.getId() + "  Name: " + level.getName());
    }

    @Override
    public String getSubEventName() {
        return "start";
    }
}
