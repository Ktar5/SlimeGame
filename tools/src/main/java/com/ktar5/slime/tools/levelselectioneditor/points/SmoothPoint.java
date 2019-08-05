package com.ktar5.slime.tools.levelselectioneditor.points;

import com.ktar5.slime.tools.levelselectioneditor.Path;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SmoothPoint extends PathPoint {
    private List<Point> subPoints;

    public SmoothPoint(Path path, int x, int y) {
        super(path, x, y);
    }

    public SmoothPoint(Path path, JSONObject json) {
        super(path, json);
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
