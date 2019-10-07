package com.ktar5.slime.screens.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.levelselection.pathing.ControlPoint;
import com.ktar5.slime.screens.levelselection.pathing.DataPoint;
import com.ktar5.slime.screens.levelselection.pathing.Path;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

@Getter
//TODO
// input
// move to selected level
// secrets
// load from saved gamedata
public class World implements Renderable {
    private Texture texture;
    private Map<UUID, Path> paths;
    private Map<UUID, ControlPoint> controlPoints;
    private List<DataPoint> dataPoints;
    private UUID startingControlPoint;
    private WorldPlayer worldPlayer;

    private String name;

    @Override
    public void render(SpriteBatch batch, float dTime) {
        batch.draw(texture, 0, 0);
        batch.end();
        SlimeGame.getGame().getShapeRenderer().setAutoShapeType(true);
        SlimeGame.getGame().getShapeRenderer().begin();
        for (ControlPoint value : controlPoints.values()) {
            SlimeGame.getGame().getShapeRenderer().circle(value.getX(), value.getY(), 3);
        }
        SlimeGame.getGame().getShapeRenderer().end();

        batch.begin();
        worldPlayer.render(batch, dTime);
    }

    public World(File saveFile, JSONObject json) {
        paths = new HashMap<>();
        controlPoints = new HashMap<>();
        dataPoints = new ArrayList<>();
        this.name = json.getString("name");
        this.texture = new Texture(Gdx.files.internal(json.getString("textureFile")));

        JSONArray jsonPaths = json.getJSONArray("paths");
        for (int i = 0; i < jsonPaths.length(); i++) {
            Path path = new Path(this, jsonPaths.getJSONObject(i));
            paths.put(path.getPathID(), path);
        }

        JSONArray jsonDataPoints = json.getJSONArray("dataPoints");
        for (int i = 0; i < jsonDataPoints.length(); i++) {
            dataPoints.add(new DataPoint(jsonDataPoints.getJSONObject(i)));
        }

        JSONArray jsonControlPoints = json.getJSONArray("controlPoints");
        for (int i = 0; i < jsonControlPoints.length(); i++) {
            ControlPoint controlPoint = new ControlPoint(jsonControlPoints.getJSONObject(i));
            controlPoints.put(controlPoint.getControlID(), controlPoint);
        }

        startingControlPoint = (json.getString("startingControlPoint").equals("null") ? null : UUID.fromString(json.getString("startingControlPoint")));
        worldPlayer = new WorldPlayer(this);
    }
}
