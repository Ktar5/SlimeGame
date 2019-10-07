package com.ktar5.slime.screens.levelselection.pathing;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class Point {
    private int x, y;

    public Point(JSONObject json) {
        this.x = json.getInt("x");
        this.y = json.getInt("y");
    }

}
