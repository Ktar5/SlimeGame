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
    private int activeLayerId = 0;
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

    //Move it closer to the top of the stack
    public void moveLayerUp(int layerId) {
        if (layerId >= layers.size() || layerId < 0) {
            return;
        }
        BaseLayer layer = layers.get(layerId + 1);
        layers.set(layerId + 1, layers.get(layerId));
        layers.set(layerId, layer);
    }

    //Move it closer to the bottom of the stack (0 being lowest)
    public void moveLayerDown(int layerId) {
        if (layerId >= layers.size() || layerId < 1) {
            return;
        }
        BaseLayer layer = layers.get(layerId - 1);
        layers.set(layerId - 1, layers.get(layerId));
        layers.set(layerId, layer);
    }

    public void setActiveLayerId(int id) {
        if (id >= layers.size()) {
            throw new RuntimeException("ERROR >> You cannot have a layer id higher than layers size");
        }
        this.activeLayerId = id;
    }

    public BaseLayer getActiveLayer() {
        return layers.get(activeLayerId);
    }

    public int idFromLayer(BaseLayer layer) {
        return layers.indexOf(layer);
    }


    public void remove(BaseLayer layer) {
        int id = idFromLayer(layer);
        layers.remove(layer);
    }
}
