package com.ktar5.analytics;

import com.ktar5.analytics.displays.AverageDeathsPerLevelOnFirstTryDisplay;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import org.bson.Document;

import java.util.function.Consumer;

@Getter
public class Main extends Application {
    public static Main instance;
    private DisplayWindow root;
    private MongoDBInstance mongo;

    public static void main(String[] args) {
        launch(args);
    }

    public Main(){
        instance = this;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TileMapTest");

        //this makes all stages close and the app exit when the main stage is closed
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        mongo = new MongoDBInstance("mongodb+srv://analytics:test@cluster0-k5pjp.mongodb.net/test?retryWrites=true", "test");

        //Initialize primary stage window and set to view scene
        root = new DisplayWindow();

        //TODO fix this cancer
        mongo.getCollection("analytics").find(new Document("session_num", 0)).limit(1).forEach(new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                //do nothing
            }
        });
        new AverageDeathsPerLevelOnFirstTryDisplay(0, 10);
    }
}
