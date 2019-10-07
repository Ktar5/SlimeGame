package com.ktar5.slime.screens.levelselection.pathing;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class PathPoint extends Point {
    private final Path path;
    private PathPoint prev, next;

    public PathPoint(Path path, JSONObject json) {
        super(json);
        this.path = path;
    }

    public void setNext(PathPoint nextPoint) {
        this.next = nextPoint;
        if (nextPoint != null) {
            nextPoint.prev = this;
        }
    }

    public void setPrev(PathPoint prevPoint) {
        this.prev = prevPoint;
        if (prevPoint != null) {
            prevPoint.next = this;
        }
    }

}
