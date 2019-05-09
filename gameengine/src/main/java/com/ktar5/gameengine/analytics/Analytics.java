package com.ktar5.gameengine.analytics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Analytics {
    private static Analytics session;

    private MongoDBInstance mongo;

    private int session_num, currentEventNumber = 0;
    private String platform, user_id, build_id;
    private String session_id = UUID.randomUUID().toString();
    private Locale locale = Locale.getDefault();

    private List<Document> events;

    private Analytics(Preferences prefs, MongoDBInstance mongo, String build_id) {
        this.mongo = mongo;
        this.build_id = build_id;

        events = new ArrayList<>();

        platform = Platform.getDefaultPlatform(Gdx.app.getType()).name();

        //preferences
        user_id = prefs.getString("analytics_user_id", null);
        if (user_id == null) {
            Gdx.app.log("Analytics", "No user id found. Generating a new one.");
            user_id = UUID.randomUUID().toString();
            prefs.putString("analytics_user_id", user_id);
        }

        session_num = prefs.getInteger("analytics_session_number", 0);
        session_num++;
        prefs.putInteger("analytics_session_number", session_num);

        prefs.flush();

        event(new SessionStartEvent());
    }

    public static void flush() {
        session.mongo.getCollection("analytics").insertMany(new ArrayList<>(session.events));
        session.events.clear();
    }

    public static void addEvent(AnalyticEvent event) {
        Analytics.session.event(event);
    }

    private void event(AnalyticEvent event) {
        Document document = new Document()
                .append("session_id", session_id)
                .append("user_id", user_id)
                .append("session_num", session_num)
                .append("platform", platform)
                .append("build_id", build_id)
                .append("country", locale.getCountry())
                .append("event_order", currentEventNumber)
                .append("event_id", event.getEventName());
        if (event.getData() != null) {
            document.append("event_data", event.getData());
        }
        currentEventNumber += 1;
        events.add(document);
    }

    public static Analytics create(String appName, MongoDBInstance mongo, String build_id) {
        session = new Analytics(Gdx.app.getPreferences(appName), mongo, build_id);
        return session;
    }

    public static Analytics get() {
        if (session == null) {
            throw new RuntimeException("Must call Analytics.create() before getting Analytics");
        }
        return session;
    }

}
