package com.ktar5.slime.world.level;

public class Chapter {
    public final String name;
    public final int firstLevelID, lastLevelID;

    public Chapter(String name, int firstLevelID, int lastLevelID) {
        this.name = name;
        this.firstLevelID = firstLevelID;
        this.lastLevelID = lastLevelID;
    }
}
