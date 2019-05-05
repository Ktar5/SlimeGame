package com.ktar5.tileeditor.tilemap.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.ktar5.tileeditor.properties.RootProperty;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.TilemapActor;
import com.ktar5.utilities.annotation.callsuper.CallSuper;
import lombok.Getter;
import org.json.JSONObject;
import org.pmw.tinylog.Logger;

@Getter
public abstract class BaseLayer {
    protected Tilemap parent;
    protected String name;
    protected RootProperty rootProperty;

    protected boolean visible;
    protected int transparency = 100; //0 = clear, 100 = opaque
    protected int xOffset, yOffset;

    public BaseLayer(Tilemap parent, JSONObject json) {
        this(parent, json.getString("name"), json.getBoolean("visible"),
                json.getJSONObject("offset").getInt("x"),
                json.getJSONObject("offset").getInt("y"));
        rootProperty.deserialize(json.getJSONObject("properties"));
        transparency = json.getInt("transparency");
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
        json.put("transparency", transparency);
        JSONObject offset = new JSONObject();
        offset.put("x", this.getXOffset());
        offset.put("y", this.getYOffset());
        json.put("offset", offset);

        return json;
    }

    public void setTransparency(int value) {
        if (value < 0 || value > 100) {
            Logger.debug("Cannot set transparency to less than 0 or great than 100, value: " + value);
            return;
        }
        transparency = value;
    }

    public void setVisible(boolean value) {
        this.visible = value;
    }

    public abstract void render(Batch batch, TilemapActor actor);

}
