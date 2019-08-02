package com.ktar5.slime.tools.levelselectioneditor.points;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SmoothPoint extends Point {
    private List<Point> subPoints;

    public SmoothPoint(int id, int x, int y) {
        super(id, x, y);
    }

    public SmoothPoint(JSONObject json) {
        super(json);
    }

    @Override
    public JSONObject serialize() {
        JSONArray objects = new JSONArray();
        for (int i = 0; i < subPoints.size(); i++) {
            objects.put(subPoints.get(i).serialize());
        }
        return super.serialize().put("subPoints", objects);
    }
}
