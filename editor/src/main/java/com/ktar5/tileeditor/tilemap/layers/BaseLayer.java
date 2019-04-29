package com.ktar5.tileeditor.tilemap.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.ktar5.tileeditor.properties.RootProperty;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.TilemapActor;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public abstract class BaseLayer {
    protected Tilemap parent;
    protected String name;
    protected RootProperty rootProperty;

    protected boolean visible;
    protected int xOffset, yOffset;

    public BaseLayer(Tilemap parent, JSONObject json) {
        this(parent, json.getString("name"), json.getBoolean("visible"),
                json.getJSONObject("offset").getInt("x"),
                json.getJSONObject("offset").getInt("y"));
        rootProperty.deserialize(json.getJSONObject("properties"));
    }

    public BaseLayer(Tilemap parent, String name, boolean visible, int xOffset, int yOffset) {
        this.parent = parent;
        this.name = name;
        this.rootProperty = new RootProperty();
        this.visible = visible;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @CallSuper
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        rootProperty.serialize(json);

        json.put("name", getName());
        json.put("visible", visible);
        JSONObject offset = new JSONObject();
        offset.put("x", this.getXOffset());
        offset.put("y", this.getYOffset());
        json.put("offset", offset);

        return json;
    }


    public abstract void render(Batch batch, TilemapActor actor);

}
