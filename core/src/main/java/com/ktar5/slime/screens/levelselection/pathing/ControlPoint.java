package com.ktar5.slime.screens.levelselection.pathing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.levelselection.World;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class ControlPoint extends Point {
    private final UUID controlID;

    private UUID pathUp, pathDown, pathLeft, pathRight;
    private String data;

    private int levelID = -1;

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


    public void parseData(String data) {
        if (data.isEmpty()) {
            return;
        }
        if (SlimeGame.getGame().getLevelHandler().getMapNameLookup().containsKey(data.toLowerCase())){
            levelID = SlimeGame.getGame().getLevelHandler().getMapNameLookup().get(data.toLowerCase());
        }
    }

    public boolean hasLevel() {
        return levelID != -1;
    }

    public void render(SpriteBatch batch, World world) {
        if (world.getWorldPlayer().getControlPointToOrAt().getControlID().equals(controlID)) {
            batch.draw(world.getHover(), getX() - (world.getHover().getWidth() / 2f), getY() - (world.getHover().getHeight() / 2f));
        } else if (hasLevel()) {
            batch.draw(world.getControl(), getX() - (world.getControl().getWidth() / 2f), getY() - (world.getControl().getHeight() / 2f));
        } else {
            batch.draw(world.getNolevel(), getX() - (world.getNolevel().getWidth() / 2f), getY() - (world.getNolevel().getHeight() / 2f));
        }
    }
}
