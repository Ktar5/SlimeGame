package com.ktar5.slime.analytics;

import com.ktar5.gameengine.analytics.AnalyticEvent;
import com.ktar5.slime.world.level.LevelData;
import org.bson.Document;

import java.util.UUID;

public abstract class LevelEvent implements AnalyticEvent {
    private UUID level;
    private String levelName;
    private int levelNum;

    public LevelEvent(LevelData data) {
        this.level = data.getUUID();
        this.levelName = data.getName();
        this.levelNum = data.getId();
    }

    @Override
    public String getEventName() {
        return "level";
    }

    public abstract String getSubEventName();

    @Override
    public Document getData() {
        return new Document()
                .append("level_session", LevelSession.currentLevelSession.toString())
                .append("level_uuid", level.toString())
                .append("level_name", levelName)
                .append("level_num", levelNum)
                .append("event_type", getSubEventName());
    }
}
