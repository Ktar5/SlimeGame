package com.ktar5.tileeditor.scene.tabs;

import com.ktar5.tileeditor.scene.sidebars.LayerSidebar;
import com.ktar5.tileeditor.scene.sidebars.properties.PropertiesSidebar;
import com.ktar5.tileeditor.scene.sidebars.tileset.TilesetSidebar;
import com.ktar5.tileeditor.tilemap.MapManager;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.TilemapActor;
import com.ktar5.tileeditor.util.CustomSplitPane;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

@Getter
public class TilemapTab extends AbstractTab {
    private PropertiesSidebar propertiesSidebar;
    private TilemapActor tilemapActor;
    private LayerSidebar layerSidebar;
    private TilesetSidebar tilesetSidebar;

    CustomSplitPane editorLeft;
    CustomSplitPane rightSidebar;
    CustomSplitPane editorRight;

    public TilemapTab(Tilemap tilemap) {
        super(tilemap.getId());
        propertiesSidebar = new PropertiesSidebar(tilemap.getRootProperty());
        propertiesSidebar.setWidth(150f);
        tilemapActor = new TilemapActor(tilemap);

        //pane
        //  properties
        //  pane
        //    editor
        //    pane
        //      layers
        //      tileset

        editorLeft = new CustomSplitPane(propertiesSidebar, tilemapActor, false);

        layerSidebar = new LayerSidebar(tilemap);
        tilesetSidebar = new TilesetSidebar(tilemap);
        rightSidebar = new CustomSplitPane(layerSidebar, tilesetSidebar, true);

        editorRight = new CustomSplitPane(editorLeft, rightSidebar, false);
        editorRight.setSplitAmount(.75f);

        getContentTable().add(editorRight).grow();

        layerSidebar.getAdapter().getSelectionManager().select(tilemap.getLayers().getActiveLayer());
    }

    @Override
    public boolean save() {
        if (!isSavable()) {
            return false;
        }
        getTabbable().save();
        return true;
    }

    @Override
    public void onClosed() {
        MapManager.get().remove(getTabId());
//        propertiesSidebar = null;
//        tilemapActor = null;
//        layerSidebar = null;
//        tilesetSidebar = null;
//        editorLeft = null;
//        rightSidebar = null;
//        editorRight = null;
    }

    @Override
    public Tabbable getTabbable() {
        return MapManager.get().getMap(getTabId());
    }

    @Override
    public void onSelect() {
    }

}
