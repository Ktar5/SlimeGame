package com.ktar5.gameengine.analytics;

import org.bson.Document;

public class SessionEndEvent implements AnalyticEvent {
    @Override
    public String getEventName() {
        return "session_end";
    }

    @Override
    public Document getData() {
        return null;
    }
}
