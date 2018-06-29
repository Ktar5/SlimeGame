package com.ktar5.slime.world;

import com.ktar5.slime.engine.camera.CameraFollow;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.player.JumpPlayer;
import com.ktar5.slime.variables.Constants;
import lombok.Getter;

@Getter
public class LoadedLevel extends Level implements Updatable {
    private JumpPlayer player;
    
    public LoadedLevel(Level level) {
        super(level.getSpawn(), level.getTileMap(), level.getLevelRef());
        this.player = new JumpPlayer(this);
        ((CameraFollow) EngineManager.get().getCameraBase()).setPosition(player.position);
    }
    
    @Override
    public void update(float v) {
        player.update(Constants.FRAME_DT);
    }
}
