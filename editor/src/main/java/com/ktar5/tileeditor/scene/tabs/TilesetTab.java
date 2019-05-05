package com.ktar5.tileeditor.scene.tabs;

import com.ktar5.tileeditor.scene.sidebars.properties.PropertiesSidebar;
import com.ktar5.tileeditor.tileset.TilesetActor;
import com.ktar5.tileeditor.tileset.TilesetManager;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TilesetTab extends AbstractTab {
    int idSelected;

    //selected tile info and selected tile properties

    private PropertiesSidebar propertiesSidebar;
    private TilesetActor tilesetActor;

    public TilesetTab(UUID tilemap) {
        super(tilemap);
//        VisSplitPane split = new VisSplitPane(
//                propertiesSidebar = new PropertiesSidebar(TilesetManager.get().getTileset(getTabId()).getRootProperty()),
//                new VisSplitPane(new Actor(), new Actor(), true),
//                true
//        );
//        getContent().add(split).width(175).growY();
//        getContentTable().debugAll();
        propertiesSidebar = new PropertiesSidebar(TilesetManager.get().getTileset(getTabId()).getRootProperty());
        tilesetActor = new TilesetActor(TilesetManager.get().getTileset(getTabId()));

        getContentTable().add(propertiesSidebar).width(175).growY();
        getContentTable().add(tilesetActor).grow();
        setDirty(false);
    }

    @Override
    public void onClosed() {
        TilesetManager.get().remove(getTabId());
    }

    @Override
    public Tabbable getTabbable() {
        return TilesetManager.get().getTileset(getTabId());
    }

    @Override
    public void onSelect() {

    }

}
