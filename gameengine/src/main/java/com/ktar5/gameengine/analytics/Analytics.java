package com.ktar5.gameengine.analytics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Analytics implements Disposable {
    private static Analytics session;

    private MongoDBInstance mongo;

    private AsyncExecutor executor = new AsyncExecutor(1);
    private int session_num, currentEventNumber = 0;
    private String platform, user_id, build_id, analytics_version;
    private String session_id = UUID.randomUUID().toString();
    private Locale locale = Locale.getDefault();
    private long sessionStartTime = System.currentTimeMillis();


    private List<Document> events;

    private Analytics(Preferences prefs, MongoDBInstance mongo, String build_id, String analytics_version) {
        this.mongo = mongo;
        this.build_id = build_id;
        this.analytics_version = analytics_version;

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
        Analytics analytics = get();

        ArrayList<Document> documents = new ArrayList<>(analytics.events);
        analytics.executor.submit(() -> {
            analytics.mongo.getCollection("analytics").insertMany(documents);
            Logger.tag("analytics").debug("Just flushed");
            return true;
        });

        analytics.events.clear();
    }

    public static void addEvent(AnalyticEvent event) {
        Analytics analytics = get();
        analytics.event(event);
    }

    private void event(AnalyticEvent event) {
        Document document = new Document()
                .append("session_id", session_id)
                .append("user_id", user_id)
                .append("session_num", session_num)
                .append("platform", platform)
                .append("build_id", build_id)
                .append("analytics_ver", analytics_version)
                .append("system_time", "$currentTime")
                .append("time_since_start", System.currentTimeMillis() - sessionStartTime)
                .append("country", locale.getCountry())
                .append("event_order", currentEventNumber)
                .append("event_id", event.getEventName());
        if (event.getData() != null) {
            document.append("event_data", event.getData());
        }
        currentEventNumber += 1;
        events.add(document);
    }

    public static Analytics create(Preferences preferences, MongoDBInstance mongo, String build_id, String analytics_version) {
        System.out.println("Created analytics");
        session = new Analytics(preferences, mongo, build_id, analytics_version);
        flush();
        return session;
    }

    public static Analytics get() {
        if (session == null) {
            throw new RuntimeException("Must call Analytics.create() before getting Analytics");
        }
        return session;
    }

    @Override
    public void dispose() {
        event(new SessionEndEvent());
        get().mongo.getCollection("analytics").insertMany(get().events);
        System.out.println("Just flushed");
        mongo.dispose();
        executor.dispose();
    }
}
