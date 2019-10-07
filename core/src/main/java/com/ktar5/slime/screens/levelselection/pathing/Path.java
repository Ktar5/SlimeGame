package com.ktar5.slime.screens.levelselection.pathing;

import com.ktar5.slime.screens.levelselection.World;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class Path {
    private UUID pathID;
    private String name;
    private PathPoint firstPoint, lastPoint;
    private UUID controlStart, controlEnd;
    private World world;

    public Path(World world, JSONObject json) {
        this.world = world;
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

    public ControlPoint getStart() {
        if (controlStart == null) {
            return null;
        }
        return world.getControlPoints().get(controlStart);
    }

    public ControlPoint getEnd() {
        if (controlEnd == null) {
            return null;
        }
        return world.getControlPoints().get(controlEnd);
    }

    public PathDirection getPathDirection(UUID controlPoint) {
        if (controlPoint.equals(controlStart)) {
            return new PathDirection(getFirstPoint(), true, getEnd());
        } else if (controlPoint.equals(controlEnd)) {
            return new PathDirection(getFirstPoint(), false, getStart());
        }else{
            return null;
        }
    }

    @AllArgsConstructor
    public static class PathDirection {
        public final PathPoint firstPoint;
        public final boolean forward;
        public final ControlPoint end;
    }

}
