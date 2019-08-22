package com.ktar5.slime.tools.levelselectioneditor.points;

import com.ktar5.slime.tools.levelselectioneditor.Main;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.utilities.common.constants.Direction;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class ControlPoint extends Point {
    private final UUID controlID;

    private UUID pathUp, pathDown, pathLeft, pathRight;
    private String data;

    public ControlPoint(String data, int x, int y) {
        super(x, y);
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

    public void removePath(UUID pathId){
        if(pathId == null){
            return;
        }
        if(pathUp != null && pathUp.equals(pathId)) pathUp = null;
        if(pathDown != null && pathDown.equals(pathId)) pathDown = null;
        if(pathLeft != null && pathLeft.equals(pathId)) pathLeft = null;
        if(pathRight != null && pathRight.equals(pathId)) pathRight = null;
    }

    public void setPath(UUID newPath, Direction direction) {
        switch (direction){
            case N:
                if(pathUp != null) {
                    removeEnds(pathUp);
                }
                this.pathUp = newPath;
            case E:
                if(pathRight != null) {
                    removeEnds(pathRight);
                }
                this.pathRight = newPath;
                break;
            case S:
                if(pathDown != null) {
                    removeEnds(pathDown);
                }
                this.pathDown = newPath;
                break;
            case W:
                if(pathLeft != null) {
                    removeEnds(pathLeft);
                }
                this.pathLeft = newPath;
                break;
        }

    }

    private void removeEnds(UUID pathID){
        Path path = Main.getInstance().mainStage.getSceneRenderer().getScene().getPaths().get(pathID);
        if(path.getControlEnd().equals(pathID)){
            path.hardSetControlEnd(null);
        }
        if(path.getControlStart().equals(pathID)){
            path.hardSetControlStart(null);
        }
    }

}
