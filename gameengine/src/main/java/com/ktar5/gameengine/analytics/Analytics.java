package com.ktar5.gameengine.analytics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import org.bson.Document;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Analytics implements Disposable {
    private static Analytics session;

    private MongoDBInstance mongo;

    private AsyncExecutor executor = new AsyncExecutor(1);
    private int session_num, currentEventNumber = 0;
    private String platform, user_id, build_id;
    private int build_version;
    private String session_id = UUID.randomUUID().toString();
    private Locale locale = Locale.getDefault();
    private long sessionStartTime = System.currentTimeMillis();

    private List<Document> events;

    public static boolean enabled = true;

    private Analytics(Preferences prefs, MongoDBInstance mongo, String build_id, int build_version, int resets) {
        if(!enabled){
            return;
        }
        Logger.tag("analytics").debug("Analytics initialized with build ID: '" + build_id + ":" + build_version);
        this.build_version = build_version;
        this.mongo = mongo;
        this.build_id = build_id;
        events = new ArrayList<>();
        platform = Platform.getDefaultPlatform(Gdx.app.getType()).name();


        int tempResets = prefs.getInteger("resets", 0);
        if(tempResets < resets){
            prefs.clear();
            prefs.putInteger("resets", resets);
            prefs.flush();
            Logger.tag("analytics").debug("Analytics reset, preferences reset");
        }

        user_id = prefs.getString("analytics_user_id", null);
        if (user_id == null) {
            Logger.tag("analytics").debug("No user id found. Generating a new one.");
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
        if(!enabled){
            return;
        }
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
        if(!enabled){
            return;
        }
        Analytics analytics = get();
        analytics.event(event);
    }

    private void event(AnalyticEvent event) {
        if(!enabled){
            return;
        }
        Document document = new Document()
                .append("session_id", session_id)
                .append("user_id", user_id)
                .append("session_num", session_num)
                .append("platform", platform)
                .append("build_id", build_id)
                .append("build_version", build_version)
                .append("system_time", System.currentTimeMillis())
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

    public static Analytics create(Preferences preferences, MongoDBInstance mongo, String build_id, int build_version, int resets) {
        if(!enabled){
            return null;
        }
        Logger.tag("analytics").debug("Created analytics");
        session = new Analytics(preferences, mongo, build_id, build_version, resets);
        flush();
        return session;
    }

    public static Analytics get() {
        if(!enabled){
            return null;
        }
        if (session == null) {
            throw new RuntimeException("Must call Analytics.create() before getting Analytics");
        }
        return session;
    }

    @Override
    public void dispose() {
        event(new SessionEndEvent());
        get().mongo.getCollection("analytics").insertMany(get().events);
        Logger.tag("analytics").debug("Just flushed");
        mongo.dispose();
        executor.dispose();
    }

    public static boolean hasInternet(){
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            Logger.debug("We have internet access");
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            Logger.debug("We do not have internet access");
            return false;
        }
    }
}
