package com.ktar5.gameengine.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class JsonUtil {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonParser parser = new JsonParser();

    public static String prettify(String jsonString) {
        return gson.toJson(parser.parse(jsonString).getAsJsonObject());
    }

}
