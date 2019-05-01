package com.ktar5.tileeditor.scene.sidebars.tileset;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.ktar5.tileeditor.scene.tabs.AbstractTab;
import com.ktar5.tileeditor.tileset.BaseTileset;
import com.ktar5.tileeditor.tileset.TilesetActor;
import com.ktar5.tileeditor.tileset.TilesetManager;
import com.ktar5.tileeditor.util.Tabbable;
import lombok.Getter;

@Getter
public class MapTilesetTab extends AbstractTab {
    private TilesetActor tilesetActor;

    public MapTilesetTab(BaseTileset tileset) {
        super(tileset.getId());
        tilesetActor = new TilesetActor(TilesetManager.get().getTileset(getTabId()));
        getContentTable().add(tilesetActor).grow();
        tilesetActor.debug();

        tilesetActor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float xF = x - tilesetActor.getPanX();
                float yF = y - tilesetActor.getPanY();
//                xF = xF / tilesetActor.getScale();
//                yF = yF / tilesetActor.getScale();
                System.out.println("Input! " + xF + " " + yF);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public String getTabTitle() {
        return getTabbable().getName().replace(".json", "");
    }

    @Override
    public Tabbable getTabbable() {
        return TilesetManager.get().getTileset(getTabId());
    }

    @Override
    public void onSelect() {

    }
}
