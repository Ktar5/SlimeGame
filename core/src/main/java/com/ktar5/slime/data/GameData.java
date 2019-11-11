package com.ktar5.slime.data;

import java.util.HashMap;
import java.util.Map;

public class GameData {
    public int deaths = 0;
    public boolean fullscreen = false;
    public Map<String, Integer> movesOnLevelMap;

    public GameData() {
        movesOnLevelMap = new HashMap<>();
    }
}
