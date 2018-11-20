package com.ktar5.slime.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.entities.player.states.Respawn;

public class Respawner implements Updatable {
    @Override
    public void update(float dTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer().getEntityState()
                    .changeStateAfterUpdate(Respawn.class);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().toggleDebug();
        }
    }
}
