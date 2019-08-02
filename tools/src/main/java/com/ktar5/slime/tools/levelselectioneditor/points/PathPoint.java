package com.ktar5.slime.tools.levelselectioneditor.points;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class PathPoint extends Point {
    private PathPoint prev, next;

    public PathPoint(int id, int x, int y) {
        super(id, x, y);
    }

    public PathPoint(JSONObject json) {
        super(json);
    }

    public void setNext(PathPoint nextPoint) {
        this.next = nextPoint;
        nextPoint.prev = this;
    }

    public void setPrev(PathPoint nextPoint) {
        this.next = nextPoint;
        nextPoint.prev = this;
    }


}
