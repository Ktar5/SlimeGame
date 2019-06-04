package com.ktar5.slime.tools.tilemapprocessor;

public class LevelLoadingChecker {
    //Layer checks
    public boolean hasGameplayLayer = false;
    public boolean gameplayLayerInstanceofTileMapLayer = false;
    public boolean hasPropertiesLayer = false;
    public boolean allLayersKnown = true;

    //GameTile checks
    public boolean hasStart = false;
    public boolean hasAtLeastOneWin = false;
}
