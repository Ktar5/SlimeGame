package com.ktar5.gameengine.analytics;

import org.bson.Document;

public interface AnalyticEvent {

    public String getEventName();

    public Document getData();

}