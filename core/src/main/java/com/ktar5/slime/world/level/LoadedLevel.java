package com.ktar5.slime.world.level;

import com.ktar5.slime.engine.camera.CameraFollow;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.variables.Constants;
import com.ktar5.slime.world.tiles.base.Tile;
import lombok.Getter;

@Getter
public class LoadedLevel extends Level implements Updatable {
    private JumpPlayer player;

    public LoadedLevel(Level level) {
        super(level.tileMap, level.levelRef);
        this.grid = level.grid;
        this.spawn = level.spawn;

        this.player = new JumpPlayer(this);
        ((CameraFollow) EngineManager.get().getCameraBase()).setPosition(player.position);
    }

    @Override
    public void update(float v) {
        player.update(Constants.FRAME_DT);
    }

    public void reset() {
        for (Tile[] tiles : this.grid.grid) {
            for (Tile tile : tiles) {
                tile.reset();
            }
        }
    }

}
