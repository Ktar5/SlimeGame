package com.ktar5.slime.tools.levelselectioneditor;

import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.PathPoint;
import com.ktar5.slime.tools.levelselectioneditor.scene.Scene;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KSerializeable;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class Path implements KSerializeable {
    public boolean updated = false;

    private Scene scene;

    private UUID pathID;
    private String name;
    @Setter
    private PathPoint firstPoint, lastPoint;
    @Setter
    private UUID controlStart, controlEnd;

    public Path(Scene scene, String name) {
        this.scene = scene;
        pathID = UUID.randomUUID();
        this.name = name;
    }

    public Path(Scene scene, JSONObject json) {
        this.scene = scene;
        pathID = UUID.fromString(json.getString("id"));
        name = json.getString("name");

        JSONArray points = json.getJSONArray("points");
        PathPoint current = null;
        for (int i = 0; i < points.length(); i++) {
            if (firstPoint == null) {
                current = new PathPoint(this, points.getJSONObject(i));
                firstPoint = current;
            } else {
                current.setNext(new PathPoint(this, points.getJSONObject(i)));
                lastPoint = current.getNext();
            }
        }
        controlStart = json.getString("start").equals("null") ? null : UUID.fromString(json.getString("start"));
        controlEnd = json.getString("end").equals("null") ? null : UUID.fromString(json.getString("end"));
    }

    public void addPoint(PathPoint point) {
        if (firstPoint == null) {
            firstPoint = point;
            lastPoint = point;
        } else {
            lastPoint.setNext(point);
            lastPoint = point;
        }
    }

    public ControlPoint getStart() {
        if (controlStart == null) {
            return null;
        }
        return scene.getControlPoints().get(controlStart);
    }

    public ControlPoint getEnd() {
        if (controlEnd == null) {
            return null;
        }
        return scene.getControlPoints().get(controlEnd);
    }

    public FrontBack getFrontBack(UUID controlPoint) {
        if (controlPoint.equals(controlStart)) {
            return FrontBack.FRONT;
        } else {
            return FrontBack.BACK;
        }
    }

    @Override
    public JSONObject serialize() {
        JSONArray objects = new JSONArray();
        PathPoint current = this.firstPoint;
        while (current != null) {
            objects.put(current.serialize());
            current = current.getNext();
        }
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("points", objects);
        json.put("id", pathID.toString());
        json.put("start", controlStart == null ? "null" : controlStart.toString());
        json.put("end", controlEnd == null ? "null" : controlEnd.toString());
        return json;
    }

    public void setName(String name){
        this.name = name;
        Main.getInstance().mainStage.getSidebar().getPathSidebar().getPathSelection().itemsChanged();
    }

}
