package com.ktar5.tileeditor.scene.sidebars.tileset;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.scene.tabs.TabHoldingPane;
import com.ktar5.tileeditor.tilemap.Tilemap;
import com.ktar5.tileeditor.tileset.Tile;
import com.ktar5.tileeditor.tileset.Tileset;
import com.ktar5.tileeditor.tileset.TilesetActor;
import com.ktar5.utilities.common.data.Pair;
import lombok.Getter;

@Getter
public class TilesetSidebar extends VisTable {
    private Tile selectedTile;

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

        for (Tileset tileset : tilemap.getTilesets().getTilesets()) {
            getTabHoldingPane().addTab(new MapTilesetTab(tileset, this));
        }
    }


    //Draw selection
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (selectedTile == null) {
            return;
        }

        MapTilesetTab currentTab = ((MapTilesetTab) tabHoldingPane.getCurrentTab());
        TilesetActor tilesetActor = currentTab.getTilesetActor();
        if (!tilesetActor.getTileset().equals(selectedTile.getTileset())) {
            return;
        }

        Pair pair = selectedTile.toXY();

        float x = tilesetActor.getRenderX();
        x = x + (pair.x * selectedTile.getTileset().getTileWidth() * tilesetActor.getScale());

        float y = tilesetActor.getRenderY() + (selectedTile.getTileset().getDimensionY() * tilesetActor.getScale());
        y = y - ((pair.y + 1) * selectedTile.getTileset().getTileHeight() * tilesetActor.getScale());


        if (!ScissorStack.pushScissors(tilesetActor.getClip())) {
            return;
        }

        batch.draw(Main.getInstance().getSelection(), (int) x, (int) y, 0, 0,
                tilesetActor.getTileset().getTileWidth(), tilesetActor.getTileset().getTileHeight(),
                tilesetActor.getScale(), tilesetActor.getScale(), 0);
        batch.flush();
        ScissorStack.popScissors();
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
    }

}
