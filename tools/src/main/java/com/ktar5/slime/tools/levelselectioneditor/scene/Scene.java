package com.ktar5.slime.tools.levelselectioneditor.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.ktar5.slime.tools.levelselectioneditor.Path;
import com.ktar5.slime.tools.levelselectioneditor.points.ControlPoint;
import com.ktar5.slime.tools.levelselectioneditor.points.DataPoint;
import com.ktar5.slime.tools.levelselectioneditor.ui.util.KSerializeable;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Getter
public class Scene implements KSerializeable {
    private Map<UUID, Path> paths;

    private Map<UUID, ControlPoint> controlPoints;
    private List<DataPoint> dataPoints;
    private UUID startingPath;

    private String name;
    private File saveFile, textureFile;

    private Texture texture;

    @Setter
    private boolean dirty = false;

    public Scene(String name, File saveFile, File textureFile) {
        paths = new HashMap<>();
        controlPoints = new HashMap<>();
        dataPoints = new ArrayList<>();
        this.name = name;
        this.saveFile = saveFile;
        this.textureFile = textureFile;

        FileHandle textureFileHandle = Gdx.files.absolute(textureFile.getAbsolutePath());
        this.texture = new Texture(textureFileHandle);
    }

    public Scene(File saveFile, JSONObject json) {
        this(json.getString("name"), saveFile,
                Paths.get(saveFile.getPath()).resolve(json.getString("textureFile")).toFile());

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

        startingPath = (json.getString("startingPath").equals("null") ? null : UUID.fromString(json.getString("startingPath")));
    }


    @Override
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        JSONArray pathsJSON = new JSONArray();
        for (Path value : paths.values()) {
            pathsJSON.put(value.serialize());
        }
        json.put("paths", pathsJSON);

        JSONArray dataPointsJSON = new JSONArray();
        for (DataPoint dataPoint : dataPoints) {
            dataPointsJSON.put(dataPoint.serialize());
        }
        json.put("dataPoints", dataPointsJSON);

        JSONArray controlPointsJSON = new JSONArray();
        for (ControlPoint controlPoint : controlPoints.values()) {
            controlPointsJSON.put(controlPoint.serialize());
        }
        json.put("controlPoints", controlPointsJSON);

        json.put("startingPath", startingPath == null ? "null" : startingPath.toString());
        json.put("name", name);

        json.put("textureFile", Paths.get(saveFile.getPath()).relativize(Paths.get(textureFile.getPath())).toString());

        return json;
    }

    public void save() {
        Logger.info("Starting save for scene (" + name + ") in " + "\"" + getSaveFile() + "\"");

        if (getSaveFile().exists()) {
            getSaveFile().delete();
        }

        try {
            getSaveFile().createNewFile();
            FileWriter writer = new FileWriter(getSaveFile());
            writer.write(serialize().toString(4));
            this.dirty = false;
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Logger.info("Finished save for scene (" + name + ") in " + "\"" + getSaveFile() + "\"");
    }

}
