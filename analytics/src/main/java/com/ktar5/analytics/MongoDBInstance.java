package com.ktar5.analytics;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class MongoDBInstance {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBInstance(String connection, String dbName) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.

        // Use a Connection String "mongodb://localhost"
        mongoClient = MongoClients.create(connection);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }

    public void dispose() {
        mongoClient.close();
    }
}
