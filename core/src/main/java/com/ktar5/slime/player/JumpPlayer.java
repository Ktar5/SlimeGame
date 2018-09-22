package com.ktar5.slime.player;


import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.PlayerEntity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.engine.statemachine.SimpleStateMachine;
import com.ktar5.slime.player.states.Idle;
import com.ktar5.slime.player.states.Move;
import com.ktar5.slime.player.states.PlayerState;
import com.ktar5.slime.player.states.Respawn;
import com.ktar5.slime.world.level.LoadedLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JumpPlayer extends PlayerEntity<PlayerState> {
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
        super(2, 1, 1);
        this.position.set(level.getSpawnX(), level.getSpawnY());
        this.level = level;
        System.out.println("New player created " + System.currentTimeMillis());
    }

    int lastX = 0;
    int lastY = 0;

    @Override
    public void update(float dTime) {
        super.update(dTime);
        getEntityState().update(dTime);
        if (lastX == (int) position.x && lastY == (int) position.y) {
            return;
        }
        lastY = (int) position.y;
        lastX = (int) position.x;
        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid().activateAllTiles(this);
    }

    public void kill() {
        if (!getEntityState().getCurrent().getClass().equals(Respawn.class)) {
            getEntityState().changeStateAfterUpdate(Respawn.class);
        }
    }

    @Override
    protected SimpleStateMachine<PlayerState> initializeStateMachine() {
        return new SimpleStateMachine<>(new Idle(this),
                new Idle(this), new Move(this), new Respawn(this));
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
