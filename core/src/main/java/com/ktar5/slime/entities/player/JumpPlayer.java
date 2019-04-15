package com.ktar5.slime.entities.player;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.PlayerEntity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.entities.player.states.Idle;
import com.ktar5.slime.entities.player.states.Move;
import com.ktar5.slime.entities.player.states.PlayerState;
import com.ktar5.slime.entities.player.states.Respawn;
import com.ktar5.slime.world.level.LoadedLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JumpPlayer extends PlayerEntity<PlayerState> {
    int lastX = 0;
    int lastY = 0;
    //This variable is for doing movement that may
    //have been pressed a few frames before it actually
    //should have been pressed
    private Vector2 previousNonZeroMovement;
    //This boolean tells whether the slime should be small
    //right now or not (for drains/holes)
    private boolean small = false;
    //The current level that this platyer is attached to
    private LoadedLevel level;

    public JumpPlayer(LoadedLevel level) {
        super(2, 16, 16);
        this.position.set(level.getSpawnX(), level.getSpawnY());
        this.level = level;
        System.out.println("New player created " + System.currentTimeMillis());
    }

    @Override
    public void update(float dTime) {
        super.update(dTime);
        position.set(position.x, position.y);
        if (!isHaltMovement()) {
            if (lastX == (int) position.x / 16 && lastY == (int) position.y / 16) {
                return;
            }
            lastY = (int) position.y / 16;
            lastX = (int) position.x / 16;
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid().activateAllTiles(this);
            boolean[][] slimeCovered = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getSlimeCovered();
            if(!slimeCovered[lastX][lastY]){
                slimeCovered[lastX][lastY] = true;
                LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
                TiledMapTileLayer mapLayer = currentLevel.getGameplayArtLayer();
                TiledMapTileSet gameplayImages = currentLevel.getTileMap().getTileSets().getTileSet("GameplayImages");
                currentLevel.addEdit(lastX, lastY, "Art_Gameplay", 0);
                if (mapLayer.getCell(lastX, lastY) == null) {
                    TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
                    newCell.setTile(gameplayImages.getTile(791));
                    mapLayer.setCell(lastX, lastY, newCell);
                } else {
                    mapLayer.getCell(lastX, lastY).setTile(gameplayImages.getTile(791));
                }
            }
        }
    }

    @Override
    public void reset() {
        setSmall(false);
        ((Respawn) getEntityState().get(Respawn.class)).cancel();
        getPosition().set(SlimeGame.getGame().getLevelHandler().getSpawnX(),
                SlimeGame.getGame().getLevelHandler().getSpawnY());
        resetAnimation("slime_jump_down");
        setHaltMovement(false);
        getEntityState().changeStateAfterUpdate(Idle.class);
    }

    public void kill() {
        if (!getEntityState().getCurrent().getClass().equals(Respawn.class)) {
            getEntityState().changeStateAfterUpdate(Respawn.class);
        }
    }

    public void setSmall(boolean value) {
        small = value;
        if (value) {
            getEntityAnimator().setUnitsX(8);
            getEntityAnimator().setUnitsY(8);
        } else {
            getEntityAnimator().setUnitsX(16);
            getEntityAnimator().setUnitsY(16);
        }
    }

    @Override
    protected SimpleStateMachine<PlayerState> initializeStateMachine() {
        return new SimpleStateMachine<>(new Idle(this),
                new Move(this), new Respawn(this));
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE, EngineManager.get().getAnimationLoader()
                .getTextureAsAnimation(this.getDefaultAnimation()), width, height);
    }

    public void resetAnimation(String newAnimation) {
        getEntityAnimator().setAnimation(EngineManager.get().getAnimationLoader().getAnimation(newAnimation));
    }

    @Override
    protected String getDefaultAnimation() {
        return "slime_jump_down";
    }

}
