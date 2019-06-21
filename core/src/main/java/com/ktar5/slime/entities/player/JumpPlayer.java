package com.ktar5.slime.entities.player;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ktar5.gameengine.analytics.Analytics;
import com.ktar5.gameengine.core.EngineManager;
import com.ktar5.gameengine.entities.components.EntityAnimator;
import com.ktar5.gameengine.entities.components.movement.Movement;
import com.ktar5.gameengine.entities.components.movement.SetVelocityMovement;
import com.ktar5.gameengine.statemachine.SimpleStateMachine;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.analytics.LevelFailEvent;
import com.ktar5.slime.entities.GameEntity;
import com.ktar5.slime.entities.Rectangle;
import com.ktar5.slime.entities.Teleportable;
import com.ktar5.slime.entities.player.states.Idle;
import com.ktar5.slime.entities.player.states.NewMove;
import com.ktar5.slime.entities.player.states.PlayerState;
import com.ktar5.slime.entities.player.states.Respawn;
import com.ktar5.slime.world.level.LoadedLevel;
import lombok.Getter;
import lombok.Setter;
import org.tinylog.Logger;

@Getter
@Setter
public class JumpPlayer extends GameEntity<PlayerState> implements Teleportable {
    private Movement movement = new SetVelocityMovement(40f);
    //This variable is for doing movement that may
    //have been pressed a few frames before it actually
    //should have been pressed
    private Vector2 previousNonZeroMovement;
    //This boolean tells whether the slime should be small
    //right now or not (for drains/holes)
    private boolean endTeleport = false;
    private boolean small = false;
    //The current level that this platyer is attached to
    private LoadedLevel level;

    public JumpPlayer(LoadedLevel level) {
        super(16, 16, new Rectangle(8, 8));
        this.position.set(8 + (level.getSpawnTile().x * 16), 8 + (level.getSpawnTile().y * 16));
        this.level = level;
        Logger.debug("New player created " + System.currentTimeMillis());
    }

    @Override
    public void update(float dTime) {
        super.update(dTime);
        position.set(position.x, position.y);
    }

    @Override
    public void reset() {
        setSmall(false);
        ((Respawn) getEntityState().get(Respawn.class)).cancel();
        getPosition().set( 8 + (SlimeGame.getGame().getLevelHandler().getSpawnX() * 16),
                8 + (SlimeGame.getGame().getLevelHandler().getSpawnY() * 16));
        resetAnimation("slime_jump_down");
        setHaltMovement(false);
        getEntityState().changeStateAfterUpdate(Idle.class);
    }

    public void kill(String cause) {
        if (!getEntityState().getCurrent().getClass().equals(Respawn.class)) {
            getEntityState().changeStateAfterUpdate(Respawn.class);
            int id = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getId();
            if (id != level.getId()) {
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
                new NewMove(this), new Respawn(this));
    }

    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE, EngineManager.get().getAnimationLoader()
                .getTextureAsAnimation(this.getDefaultAnimation()), width, height);
    }

    @Override
    public void debugRender(ShapeRenderer renderer) {
        renderer.rect(
                position.x - ((float) getHitbox().width / 2), position.y - ((float) getHitbox().height / 2),
                getHitbox().width, getHitbox().height);
        renderer.rect(position.x, position.y, 2, 2);
    }

    public void resetAnimation(String newAnimation) {
        getEntityAnimator().setAnimation(EngineManager.get().getAnimationLoader().getAnimation(newAnimation));
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    protected String getDefaultAnimation() {
        return "slime_jump_down";
    }

    @Override
    public void setTeleporting(boolean teleporting) {
        endTeleport = teleporting;
    }

    @Override
    public boolean isTeleporting() {
        return endTeleport;
    }
}
