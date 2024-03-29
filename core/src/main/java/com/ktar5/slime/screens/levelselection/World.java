package com.ktar5.slime.screens.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ktar5.gameengine.util.Renderable;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.screens.levelselection.pathing.ControlPoint;
import com.ktar5.slime.screens.levelselection.pathing.DataPoint;
import com.ktar5.slime.screens.levelselection.pathing.Path;
import com.ktar5.slime.screens.levelselection.pathing.PathPoint;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

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

    private Texture hover, control, nolevel;

    @Override
    public void render(SpriteBatch batch, float dTime) {
        batch.draw(texture, 0, 0);
        for (ControlPoint value : controlPoints.values()) {
            value.render(batch, this);
        }

        batch.end();
        SlimeGame.getGame().getShapeRenderer().setAutoShapeType(true);
        SlimeGame.getGame().getShapeRenderer().setProjectionMatrix(SlimeGame.getGame().getGameCamera().getCamera().combined);
        SlimeGame.getGame().getShapeRenderer().begin();
        PathPoint current;
        for(Path path : paths.values()){
            current = path.getFirstPoint();
            while (current != null){
                SlimeGame.getGame().getShapeRenderer().circle(current.getX(), current.getY(), 2);
                current = current.getNext();
            }
        }

        SlimeGame.getGame().getShapeRenderer().end();

        batch.begin();
        worldPlayer.render(batch, dTime);
    }

    public World(JSONObject json) {
        hover = new Texture("textures/levelselectiontest/hover.png");
        nolevel = new Texture("textures/levelselectiontest/nolevel.png");
        control = new Texture("textures/levelselectiontest/controlpoint.png");

        paths = new HashMap<>();
        controlPoints = new HashMap<>();
        dataPoints = new ArrayList<>();
        this.name = json.getString("name");
        this.texture = new Texture(Gdx.files.internal("maps/levelselect/fsdf/" + json.getString("textureFile")));

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
