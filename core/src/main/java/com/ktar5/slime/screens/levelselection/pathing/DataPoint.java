package com.ktar5.slime.screens.levelselection.pathing;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class DataPoint extends Point {
    private String data;
    private String name;

    public DataPoint(JSONObject json) {
        super(json);
        this.data = json.getString("data");
        this.name = json.getString("name");
    }
}
