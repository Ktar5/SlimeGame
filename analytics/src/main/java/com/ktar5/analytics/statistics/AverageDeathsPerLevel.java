package com.ktar5.analytics.statistics;

import com.ktar5.analytics.Main;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AverageDeathsPerLevel {
    /*
    Steps:
    - Select all level start events
    - Select all death events
    - For each death event, determine level id
    -
     */

    private HashMap<String, Data> userMap = new HashMap<>();

    private class Data {
        //level session id , level session
        Map<String, LevelSession> listOfLevelSessions = new HashMap<>();
    }

    private class LevelSession {
        boolean complete = false;
        int numberOfFails = 0;
    }

    public double value;

    public AverageDeathsPerLevel(int levelId) {
        MongoCollection<Document> analytics = Main.instance.getMongo().getCollection("analytics");
        Document queryOne = new Document();
        queryOne.append("event_data.level_num", levelId);

        FindIterable<Document> documents = analytics.find(queryOne)
                .projection(new Document("user_id", 1)
                        .append("event_data.event_type", 1)
                        .append("event_data.level_session", 1)
                );

        documents.forEach((Consumer<Document>) document -> {
            String user_id = document.getString("user_id");
            if (!userMap.containsKey(user_id)) {
                userMap.put(user_id, new Data());
            }

            Data userData = userMap.get(user_id);
            Map<String, LevelSession> data = userData.listOfLevelSessions;

            String eventType = document.get("event_data", Document.class).getString("event_type");
            String levelSessionString = document.get("event_data", Document.class).getString("level_session");

            if (eventType.equals("fail")) {
                if (data.containsKey(levelSessionString)) {
                    data.get(levelSessionString).numberOfFails += 1;
                } else {
                    LevelSession levelSession = new LevelSession();
                    levelSession.numberOfFails += 1;
                    data.put(levelSessionString, levelSession);
                }
            } else if (eventType.equals("complete")) {
                if (data.containsKey(levelSessionString)) {
                    data.get(levelSessionString).complete = true;
                } else {
                    LevelSession levelSession = new LevelSession();
                    levelSession.complete = true;
                    data.put(levelSessionString, levelSession);
                }
            }
        });


        int total = 0;
        int numberOfCompletedSessions = 0;
        for (Data data : userMap.values()) {
            for (LevelSession levelSession : data.listOfLevelSessions.values()) {
                if (levelSession.complete) {
                    total += levelSession.numberOfFails;
                    numberOfCompletedSessions += 1;
                }
            }
        }

        value = (double) total / numberOfCompletedSessions;
    }

}
