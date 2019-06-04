package com.ktar5.slime.entities.player;


import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.PlayerEntity;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelFailEvent;
import com.ktar5.slime.entities.player.states.Idle;
import com.ktar5.slime.entities.player.states.Move;
import com.ktar5.slime.entities.player.states.PlayerState;
import com.ktar5.slime.entities.player.states.Respawn;
import com.ktar5.slime.world.level.LoadedLevel;
import lombok.Getter;
import lombok.Setter;
import org.tinylog.Logger;

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
        this.position.set(level.getSpawnTile().x * 16, level.getSpawnTile().y * 16);
        this.level = level;
        Logger.debug("New player created " + System.currentTimeMillis());
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
            SlimeGame.getGame().getLevelHandler().getCurrentLevel().activateAllTiles(this);
//            boolean[][] slimeCovered = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getSlimeCovered();

            //TODO implement this code everywhere
//            if (!slimeCovered[lastX][lastY]) {
//                slimeCovered[lastX][lastY] = true;
//                LoadedLevel currentLevel = SlimeGame.getGame().getLevelHandler().getCurrentLevel();
//                currentLevel.incrementSlimeCovered();
//                TiledMapTileLayer mapLayer = (TiledMapTileLayer) currentLevel.getTileMap().getLayers().get("SlimeCover");
//                TiledMapTileSets tileSets = currentLevel.getTileMap().getTileSets();
//                int i = tileSets.getTileSet("GameplayImages").getProperties().get("firstgid", Integer.class);
//                if (mapLayer.getCell(lastX, lastY) == null) {
//                    TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
//                    newCell.setTile(tileSets.getTile(i + 207));
//                    mapLayer.setCell(lastX, lastY, newCell);
//                    currentLevel.addEdit(lastX, lastY, "SlimeCover", 0);
//                } else {
//                    if(mapLayer.getCell(lastX, lastY).getTile() == null){
//                        currentLevel.addEdit(lastX, lastY, "SlimeCover", 0);
//                    }else {
//                        currentLevel.addEdit(lastX, lastY, "SlimeCover", mapLayer.getCell(lastX, lastY).getTile().getId());
//                    }
//                    mapLayer.getCell(lastX, lastY).setTile(tileSets.getTile(i + 207));
//                }
//            }
        }
    }

    @Override
    public void reset() {
        setSmall(false);
        ((Respawn) getEntityState().get(Respawn.class)).cancel();
        getPosition().set(SlimeGame.getGame().getLevelHandler().getSpawnX() * 16,
                SlimeGame.getGame().getLevelHandler().getSpawnY() * 16);
        resetAnimation("slime_jump_down");
        setHaltMovement(false);
        getEntityState().changeStateAfterUpdate(Idle.class);
    }

    public void kill(String cause) {
        if (!getEntityState().getCurrent().getClass().equals(Respawn.class)) {
            getEntityState().changeStateAfterUpdate(Respawn.class);
            int id = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getId();
            if(id != level.getId()){
                throw new RuntimeException("LevelData not equal id");
            }
            Analytics.addEvent(new LevelFailEvent(this, cause));
//            SlimeGame.getGame().getGameAnalytics().submitProgressionEvent(GameAnalytics.ProgressionStatus.Fail,
//                    String.valueOf(SlimeGame.getGame().getLevelHandler().getCurrentLevel().getId()), "", "");
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
