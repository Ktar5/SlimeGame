package com.ktar5.slime.tools.levelselectioneditor.points;

import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class ControlPoint extends Point {
    private final UUID controlID;
    private UUID pathUp, pathDown, pathLeft, pathRight;
    private String data;

    public ControlPoint(String data, int id, int x, int y) {
        super(id, x, y);
        this.controlID = UUID.randomUUID();
        this.data = data;
    }

    public ControlPoint(JSONObject json) {
        super(json);
        this.data = json.getString("data");
        this.controlID = UUID.fromString(json.getString("controlID"));

        JSONObject paths = json.getJSONObject("paths");
        pathUp = (paths.getString("pathUp") == null ? null : UUID.fromString(paths.getString("pathUp")));
        pathDown = (paths.getString("pathDown") == null ? null : UUID.fromString(paths.getString("pathDown")));
        pathLeft = (paths.getString("pathLeft") == null ? null : UUID.fromString(paths.getString("pathLeft")));
        pathRight = (paths.getString("pathRight") == null ? null : UUID.fromString(paths.getString("pathRight")));

    }

    @Override
    public JSONObject serialize() {
        JSONObject paths = new JSONObject();
        paths.put("pathUp", pathUp == null ? "null" : pathUp.toString());
        paths.put("pathDown", pathDown == null ? "null" : pathDown.toString());
        paths.put("pathLeft", pathLeft == null ? "null" : pathLeft.toString());
        paths.put("pathRight", pathRight == null ? "null" : pathRight.toString());

        return super.serialize().put("data", data).put("paths", paths).put("controlID", controlID.toString());
    }
}
