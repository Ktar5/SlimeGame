package com.ktar5.slime.entities.player.states;

import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.grid.Grid;
import com.ktar5.slime.world.Levels;

public class Move extends PlayerState {
    
    
    @Override
    public void start() {
    
    }
    
    @Override
    public void update(float dTime) {
        int x = (int) (getPlayer().getPosition().x);
        int y = (int) (getPlayer().getPosition().y);
        Vector2 input = getPlayer().getMovement().getInput();
        if (input.x != 0) {
            x += input.x;
        } else {
            y += input.y;
        }
        final Grid grid = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid();
        if (!grid.isInMapRange(x, y)) {
            return;
        }
        switch (grid.grid[x][y].getType()) {
            case AIR:
                getPlayer().getPosition().set(x, y);
                break;
            case WIN:
                int ordinal = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getLevelRef().ordinal();
                ordinal += 1;
                if (ordinal > Levels.values().length - 1) {
                    ordinal = 0;
                }
                SlimeGame.getGame().getLevelHandler().setLevel(Levels.values()[ordinal]);
                break;
            case WALL:
                this.changeState(Idle.class);
                break;
            case HARMFUL:
                this.changeState(Respawn.class);
                break;
        }
    }
    
    @Override
    protected void end() {
    
    }
}
