package com.ktar5.tileeditor.scene.tabs;

import com.ktar5.tileeditor.scene.sidebars.LayerSidebar;
import com.ktar5.tileeditor.scene.sidebars.properties.PropertiesSidebar;
import com.ktar5.tileeditor.tilemap.MapManager;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.TilemapActor;
import com.ktar5.tileeditor.tileset.TilesetActor;
import com.ktar5.tileeditor.util.CustomSplitPane;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

@Getter
public class TilemapTab extends AbstractTab {
    private PropertiesSidebar propertiesSidebar;
    private TilemapActor tilemapActor;

    CustomSplitPane editorLeft;
    CustomSplitPane rightSidebar;
    CustomSplitPane editorRight;

    public TilemapTab(Tilemap tilemap) {
        super(tilemap.getId());
        propertiesSidebar = new PropertiesSidebar(tilemap.getRootProperty());
        tilemapActor = new TilemapActor(tilemap);

        //pane
        //  properties
        //  pane
        //    editor
        //    pane
        //      layers
        //      tileset


        editorLeft = new CustomSplitPane(propertiesSidebar, tilemapActor, false);

        rightSidebar = new CustomSplitPane(
                new LayerSidebar(tilemap),
                new TilesetActor(tilemap.getTilesets().getTileset(0), false),
                true
        );

        editorRight = new CustomSplitPane(editorLeft, rightSidebar, false);


//        getContentTable().add(propertiesSidebar).width(175).growY();
//        getContentTable().add(tilemapActor).grow();


//        split.debugAll();

        getContentTable().add(editorRight).grow();
        lastAWidth = propertiesSidebar.getWidth();
    }

    float lastRightSplit = .5f;
    float lastAWidth;
    public void update(){
//        if(editorRight.getSplit() != lastRightSplit){
//            editorLeft.setSplitAmount(lastAWidth / editorLeft.getWidth());
//            lastRightSplit = editorRight.getSplit();
//            lastAWidth = propertiesSidebar.getWidth();
//        }
    }

    @Override
    public Tabbable getTabbable() {
        return MapManager.get().getMap(getTabId());
    }

    @Override
    public void onSelect() {
    }

}
