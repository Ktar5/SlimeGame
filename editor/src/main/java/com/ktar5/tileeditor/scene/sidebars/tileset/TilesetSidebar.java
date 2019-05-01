package com.ktar5.tileeditor.scene.sidebars.tileset;

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.ktar5.tileeditor.scene.tabs.TabHoldingPane;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tilemap.whole.WholeTile;
import com.ktar5.tileeditor.tileset.BaseTileset;
import lombok.Getter;

@Getter
public class TilesetSidebar extends VisTable {
    private WholeTile selectedTile;

    private VisTable contentTable;
    private TabHoldingPane tabHoldingPane;

    public TilesetSidebar(Tilemap tilemap) {
        //Add tab holding pane
        tabHoldingPane = new TabHoldingPane();
        add(tabHoldingPane.getTable()).expandX().fillX().top();
        row();

        contentTable = new VisTable();
        tabHoldingPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                contentTable.clearChildren();
                contentTable.add(tab.getContentTable()).expand().fill();
            }
        });
        add(contentTable).grow();

        for (BaseTileset tileset : tilemap.getTilesets().getTilesets()) {
            getTabHoldingPane().addTab(new MapTilesetTab(tileset));
        }
    }

    public void setSelectedTile(WholeTile tile) {
        this.selectedTile = tile;
    }

}
