package com.ktar5.gameengine.analytics;

import com.badlogic.gdx.utils.Disposable;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoDBInstance implements Disposable {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBInstance(String connection, String dbName) {
        // Use a Connection String "mongodb://localhost"
        mongoClient = MongoClients.create(connection);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoCollection<Document> getCollection(String collection) {
        return database.getCollection(collection);
    }

    @Override
    public void dispose() {
        mongoClient.close();
    }
}
