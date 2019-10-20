package com.ktar5.slime.screens.levelselection.pathing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ktar5.slime.screens.levelselection.World;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class ControlPoint extends Point {
    private final UUID controlID;

    private UUID pathUp, pathDown, pathLeft, pathRight;
    private String data;

    //TODO level data reference

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
        //TODO load level data reference
    }

    //TODO
    public boolean hasLevel(){
        return false;
    }

    public void render(SpriteBatch batch, World world) {
        if(world.getWorldPlayer().getControlPointToOrAt().getControlID().equals(controlID)){
            batch.draw(world.getHover(), getX() - (world.getHover().getWidth() / 2f), getY() - (world.getHover().getHeight() / 2f));
        }else if(hasLevel()){
            batch.draw(world.getControl(), getX() - (world.getControl().getWidth() / 2f), getY() - (world.getControl().getHeight() / 2f));
        }else{
            batch.draw(world.getNolevel(), getX() - (world.getNolevel().getWidth() / 2f), getY() - (world.getNolevel().getHeight() / 2f));
        }
    }
}
