package com.ktar5.tileeditor.tilemap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.ktar5.tileeditor.tilemap.layers.BaseLayer;
import com.ktar5.tileeditor.tilemap.layers.TileLayer;
import lombok.Getter;
import org.json.JSONArray;

import java.util.ArrayList;

@Getter
public class Layers {
    private Tilemap parent;
    private ArrayList<BaseLayer> layers;

    public Layers(Tilemap parent) {
        this.parent = parent;
        layers = new ArrayList<>();
    }

    public void deserialize(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            layers.add(new TileLayer(getParent(), jsonArray.getJSONObject(i)));
        }
    }

    public JSONArray serialize() {
        JSONArray json = new JSONArray();
        for (int i = 0; i < layers.size(); i++) {
            json.put(i, layers.get(i).serialize());
        }
        return json;
    }

    public void render(Batch batch, TilemapActor actor) {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).render(batch, actor);
        }
    }
}
