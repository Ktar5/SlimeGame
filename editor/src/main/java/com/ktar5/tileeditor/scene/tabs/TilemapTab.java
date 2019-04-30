package com.ktar5.tileeditor.scene.tabs;

import com.ktar5.tileeditor.scene.sidebars.properties.PropertiesSidebar;
import com.ktar5.tileeditor.tilemap.MapManager;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.TilemapActor;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

@Getter
public class TilemapTab extends AbstractTab {
    private PropertiesSidebar propertiesSidebar;
    private TilemapActor tilemapActor;

    public TilemapTab(Tilemap tilemap) {
        super(tilemap.getId());
        propertiesSidebar = new PropertiesSidebar(tilemap.getRootProperty());
        tilemapActor = new TilemapActor(tilemap);

//        VisSplitPane split = new VisSplitPane(
//                propertiesSidebar = new PropertiesSidebar(MapManager.get().getMap(getTabId()).getRootProperty()),
//                new VisSplitPane(new Actor(), new Actor(), true),
//                true
//        );

        getContentTable().add(propertiesSidebar).width(175).growY();
        getContentTable().add(tilemapActor).grow();
        getContentTable().add(propertiesSidebar).width(175).growY();
    }

    @Override
    public Tabbable getTabbable() {
        return MapManager.get().getMap(getTabId());
    }

    @Override
    public void onSelect() {
    }
//
//    @Listener
//    public static class WholeTilemapTab extends TilemapTab {
//        public WholeTilemapTab(UUID tilemap) {
//            super(tilemap);
//            EventCoordinator.get().registerListener(this);
//        }
//
//        @Handler
//        public void onSelectTile(TileSelectEvent event) {
//            if (event.getTab().equals(this.getTabId())) {
//                Image image = event.getTileset().getTileImages().get(event.getId());
//                getTilesetSidebar().getSelectedTileView().setTile(new PixelatedImageView(image));
//                WholeTileLayer tilemap = ((WholeTileLayer) getTabbable());
//                tilemap.setCurrentData(event.getId(), 0);
//            }
//        }
//    }

}
