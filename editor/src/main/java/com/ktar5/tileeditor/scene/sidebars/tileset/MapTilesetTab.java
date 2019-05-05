package com.ktar5.tileeditor.scene.sidebars.tileset;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.ktar5.tileeditor.scene.tabs.AbstractTab;
import com.ktar5.tileeditor.tileset.Tile;
import com.ktar5.tileeditor.tileset.Tileset;
import com.ktar5.tileeditor.tileset.TilesetActor;
import com.ktar5.tileeditor.tileset.TilesetManager;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

@Getter
public class MapTilesetTab extends AbstractTab {
    private TilesetActor tilesetActor;

    public MapTilesetTab(Tileset tileset, TilesetSidebar sidebar) {
        super(tileset.getId());
        tilesetActor = new TilesetActor(TilesetManager.get().getTileset(getTabId()));
        getContentTable().add(tilesetActor).grow();
        tilesetActor.debug();

        //TOP LEFT IS 0, 0
        tilesetActor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT) {
                    return false;
                }
                float xF = x - tilesetActor.getRenderX();
                xF = xF / (tilesetActor.getScale() * tilesetActor.getTileset().getTileWidth());

                float yF = y - tilesetActor.getRenderY();
                yF = yF - (tilesetActor.getTileset().getDimensionY() * tilesetActor.getScale());
                yF = yF / (tilesetActor.getScale() * tilesetActor.getTileset().getTileHeight());
                yF = Math.abs(yF);

                Tile tileFromXY = tileset.getTileFromXY((int) xF, (int) yF);
                if (tileFromXY == null) {
                    return false;
                }

                sidebar.setSelectedTile(tileFromXY);
                return true;
            }
        });
    }

    @Override
    public boolean isCloseableByUser() {
        return false;
    }

    @Override
    public String getTabTitle() {
        return getTabbable().getName().replace(".json", "");
    }

    @Override
    public void onClosed() {

    }

    @Override
    public Tabbable getTabbable() {
        return TilesetManager.get().getTileset(getTabId());
    }

    @Override
    public void onSelect() {

    }
}
