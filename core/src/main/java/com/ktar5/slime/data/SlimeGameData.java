package com.ktar5.slime.data;

import com.ktar5.gameengine.util.GameData;
import org.json.JSONObject;

import java.io.File;

public class SlimeGameData extends GameData {
    public int deaths = 0;
    public boolean fullscreen = false;

    public SlimeGameData() {
        super(new File(System.getProperty("user.home")
                + File.separator + "documents"
                + File.separator + "Wildmagic Studios"
                + File.separator + "A Story of Slime"));
    }

    @Override
    protected JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("deaths", deaths);
        json.put("fullscreen", fullscreen);
        return json;
    }

    @Override
    protected void deserialize(JSONObject json) {
        deaths = json.getInt("deaths");
    }
}
