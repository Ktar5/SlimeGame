package com.ktar5.analytics;

import com.ktar5.analytics.statistics.AverageDeathsPerLevelOnFirstTry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;

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

        new AverageDeathsPerLevelOnFirstTry(9);
    }
}
