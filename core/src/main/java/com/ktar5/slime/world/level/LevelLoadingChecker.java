package com.ktar5.slime.world.level;

public class LevelLoadingChecker {
    //Layer checks
    public boolean hasGameplayLayer = false;
    public boolean gameplayLayerInstanceofTileMapLayer = false;
    public boolean hasPropertiesLayer = false;
    public boolean allLayersKnown = true;

    //Tile checks
    public boolean hasStart = false;
    public boolean hasAtLeastOneWin = false;

    //Errors:
    public boolean moreThanOneGameplayLayer = false;
    public boolean moreThanOnePropertiesLayer = false;

}
