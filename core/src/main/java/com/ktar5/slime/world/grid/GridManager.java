package com.ktar5.slime.world.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.rendering.Renderable;
import com.ktar5.slime.engine.util.Updatable;
import com.ktar5.slime.world.grid.tiles.Tile;
import lombok.Getter;

@Getter
@Deprecated
public class GridManager implements Updatable, Renderable {
    private Grid grid;
    
    public GridManager() {
        grid = new Grid(32, 32);
    }
    
    @Override
    public void update(float dTime) {
        grid.update(dTime);
    }
    
    public static final int radius = 6;
    
    @Override
    public void render(SpriteBatch batch, float dTime) {
        int x = (int) (EngineManager.get().getCameraBase().getCamera().position.x);
        int y = (int) (EngineManager.get().getCameraBase().getCamera().position.y);
        //Draw walls and floors
        TextureRegion wall = EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/test/tiles16/tile_16_039.png").getKeyFrame(0);
        TextureRegion air = EngineManager.get().getAnimationLoader().getTextureAsAnimation("textures/test/testPlayer.jpg").getKeyFrame(0);
        
        for (int i = x - radius; i < x + radius; i++) {
            for (int k = y - radius; k < y + radius; k++) {
                if (!grid.isInMapRange(i, k)) {
                    continue;
                }
                Tile tile = grid.grid[i][k];
//                batch.draw(tile.getType(Side.DOWN) == TileType.WALL ? wall : air,
//                        tile.x - .5f, tile.y - .5f, 0, 0, 16, 16,
//                        1 / Const.SCALE, 1 / Const.SCALE, 0
//                );
            }
        }
    }
    
    @Override
    public void debug(float dTime) {
    
    }
}
