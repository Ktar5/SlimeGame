package com.ktar5.slime.screens.levelselection.pathing;

import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class ControlPoint extends Point {
    private final UUID controlID;

    private UUID pathUp, pathDown, pathLeft, pathRight;
    private String data;

    public ControlPoint(JSONObject json) {
        super(json);
        this.controlID = UUID.fromString(json.getString("controlID"));

        JSONObject paths = json.getJSONObject("paths");
        pathUp = (paths.getString("pathUp").equals("null") ? null : UUID.fromString(paths.getString("pathUp")));
        pathDown = (paths.getString("pathDown").equals("null") ? null : UUID.fromString(paths.getString("pathDown")));
        pathLeft = (paths.getString("pathLeft").equals("null") ? null : UUID.fromString(paths.getString("pathLeft")));
        pathRight = (paths.getString("pathRight").equals("null") ? null : UUID.fromString(paths.getString("pathRight")));
        this.data = json.getString("data");
        parseData(data);
    }


    public void parseData(String data){

    }


}
