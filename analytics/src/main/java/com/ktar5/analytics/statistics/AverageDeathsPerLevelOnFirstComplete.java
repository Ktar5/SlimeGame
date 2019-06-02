package com.ktar5.analytics.statistics;

import com.ktar5.analytics.Main;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AverageDeathsPerLevelOnFirstComplete {
    /*
    Steps:
    O- Select all level start events with level at certain level
    O- filter by each user
    O- for each user filter start events with completion events attached
    O- find earliest session number with event
    O- if multiple events from session, select start with lowest order number
    O- count forward the amount of fails until reach a complete
    O- repeat for each user
    - Average it
     */
    private HashMap<String, Data> userMap = new HashMap<>();
    private List<Integer> numbersToAverage = new ArrayList<>();


    //TODO optimize it by checking earliestSession as we go through the data
    private class Data {
        //session id , level session
        LevelSession earliestSession;
        Map<String, LevelSession> listOfLevelSessions = new HashMap<>();
    }

    private class LevelSession {
        int sessionNumber = 0, startOrderNumber = 0;
        int numberOfFails = 0;
        boolean complete = false;
    }

    public double value;

    public AverageDeathsPerLevelOnFirstComplete(int levelId) {
        MongoCollection<Document> analytics = Main.instance.getMongo().getCollection("analytics");
        Document queryOne = new Document();
        queryOne.append("event_data.level_num", levelId);
        queryOne.append("event_data.level_session", new Document("$exists", true));

        FindIterable<Document> documents = analytics.find(queryOne)
                .projection(new Document("user_id", 1)
                        .append("event_data.event_type", 1)
                        .append("event_data.level_session", 1)
                        .append("session_num", 1)
                        .append("event_order", 1)
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
            switch (eventType) {
                case "fail":
                    if (data.containsKey(levelSessionString)) {
                        data.get(levelSessionString).numberOfFails += 1;
                    } else {
                        LevelSession levelSession = new LevelSession();
                        levelSession.numberOfFails += 1;
                        data.put(levelSessionString, levelSession);
                    }
                    break;
                case "start":
                    if (data.containsKey(levelSessionString)) {
                        data.get(levelSessionString).sessionNumber = document.getInteger("session_num");
                        data.get(levelSessionString).startOrderNumber = document.getInteger("event_order");
                    } else {
                        LevelSession levelSession = new LevelSession();
                        levelSession.sessionNumber = document.getInteger("session_num");
                        levelSession.startOrderNumber = document.getInteger("event_order");
                        data.put(levelSessionString, levelSession);
                    }
                    break;
                case "complete":
                    if (data.containsKey(levelSessionString)) {
                        data.get(levelSessionString).complete = true;
                    } else {
                        LevelSession levelSession = new LevelSession();
                        levelSession.complete = true;
                        data.put(levelSessionString, levelSession);
                    }
                    break;
            }
        });

        //Find earliest session for each user
        for (Data value : userMap.values()) {
            LevelSession earliest = null;
            if (value.listOfLevelSessions.isEmpty()) {
                continue;
            }
            for (LevelSession levelSession : value.listOfLevelSessions.values()) {
                if(!levelSession.complete){
                    continue;
                }
                if (earliest == null) {
                    earliest = levelSession;
                } else if (levelSession.sessionNumber < earliest.sessionNumber) {
                    earliest = levelSession;
                } else if (levelSession.sessionNumber == earliest.sessionNumber) {
                    if (levelSession.startOrderNumber < earliest.startOrderNumber) {
                        earliest = levelSession;
                    }
                }
            }
            if (earliest != null) {
                numbersToAverage.add(earliest.numberOfFails);
            }
        }

        int total = 0;
        for (Integer integer : numbersToAverage) {
            total += integer;
        }
        value = (double) total / numbersToAverage.size();
    }


}