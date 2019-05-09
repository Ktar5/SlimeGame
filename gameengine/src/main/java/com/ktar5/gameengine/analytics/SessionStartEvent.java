package com.ktar5.gameengine.analytics;

import org.bson.Document;

public class SessionStartEvent implements AnalyticEvent {
    @Override
    public String getEventName() {
        return "session_start";
    }

    @Override
    public Document getData() {
        return null;
    }
}
