package com.ktar5.slime.tools.levelselectioneditor.points;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class DataPoint extends Point implements DataHaver {
    private String data;
    private String name;

    public DataPoint(String data, String name, int x, int y) {
        super(x, y);
        this.data = data;
        this.name = name;
    }

    public DataPoint(JSONObject json) {
        super(json);
        this.data = json.getString("data");
        this.name = json.getString("name");
    }

    @Override
    public JSONObject serialize() {
        return super.serialize().put("data", data).put("name", name);
    }

}
