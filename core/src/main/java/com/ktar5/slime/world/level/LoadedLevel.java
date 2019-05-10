package com.ktar5.slime.world.level;

import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.camera.CameraFollow;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.Entity;
import com.ktar5.gameengine.util.Updatable;
import com.ktar5.slime.analytics.LevelStartEvent;
import com.ktar5.slime.entities.EntityData;
import com.ktar5.slime.entities.player.JumpPlayer;
import com.ktar5.slime.variables.Constants;
import com.ktar5.slime.world.tiles.base.Tile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class LoadedLevel extends LevelData implements Updatable {
    private JumpPlayer player;
    private ArrayList<LevelEdit> edits;
    private List<Entity> entities;
    private int collectibles = 0;
    private int numberTilesSlimed = 0;

    public LoadedLevel(LevelData levelData) {
        super(levelData);

        this.edits = new ArrayList<>();

        this.entities = new ArrayList<>();
        for (EntityData initialEntityDatum : this.getInitialEntityData()) {
            entities.add(initialEntityDatum.create());
        }

        this.player = new JumpPlayer(this);
        ((CameraFollow) EngineManager.get().getCameraBase()).setPosition(player.position);

//        if(SlimeGame.getGame().getGameAnalytics() != null){
        Analytics.addEvent(new LevelStartEvent(UUID.randomUUID(), "levelData" + getId(), getId()));
//            SlimeGame.getGame().getGameAnalytics().submitProgressionEvent(GameAnalytics.ProgressionStatus.Start,
//                    String.valueOf(getId()),"", "");
//        }
    }

    @Override
    public void update(float v) {
        player.update(Constants.FRAME_DT);
        for (Entity entity : entities) {
            entity.update(Constants.FRAME_DT);
        }
        grid.update(Constants.FRAME_DT);
    }

    public LevelEdit addEdit(int x, int y, String layer, int oldID) {
        LevelEdit levelEdit = new LevelEdit.StringLevelEdit(x, y, layer, oldID);
        edits.add(levelEdit);
        return levelEdit;
    }

    public LevelEdit addEdit(int x, int y, int layer, int oldID) {
        LevelEdit levelEdit = new LevelEdit.IntLevelEdit(x, y, layer, oldID);
        edits.add(levelEdit);
        return levelEdit;
    }

    public void reset() {
        for (Tile[] tiles : this.grid.grid) {
            for (Tile tile : tiles) {
                if (tile != null) {
                    tile.reset();
                }
            }
        }
        for (int x = 0; x < getSlimeCovered().length; x++) {
            for (int y = 0; y < getSlimeCovered()[x].length; y++) {
                getSlimeCovered()[x][y] = false;
            }
        }
        for (LevelEdit edit : edits) {
            edit.undo(tileMap);
        }

        collectibles = 0;
        numberTilesSlimed = 0;

        entities.clear();
        for (EntityData initialEntityDatum : this.getInitialEntityData()) {
            entities.add(initialEntityDatum.create());
        }
        player.reset();
    }

    public void incrementSlimeCovered() {
        numberTilesSlimed += 1;
    }
}
