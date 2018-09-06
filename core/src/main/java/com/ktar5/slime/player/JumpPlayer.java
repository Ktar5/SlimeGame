package com.ktar5.slime.player;


import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.PlayerEntity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.player.states.Idle;
import com.ktar5.slime.player.states.Move;
import com.ktar5.slime.player.states.PlayerState;
import com.ktar5.slime.player.states.Respawn;
import com.ktar5.slime.utils.Side;
import com.ktar5.slime.utils.statemachine.SimpleStateMachine;
import com.ktar5.slime.world.level.LoadedLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringExclude;

@Getter
@Setter
public class JumpPlayer extends PlayerEntity {
    @ToStringExclude
    public final SimpleStateMachine<PlayerState> playerState;

    private Side lastMovedDirection = Side.UP;

    //This variable is for doing movement that may
    //have been pressed a few frames before it actually
    //should have been pressed
    private Vector2 previousNonZeroMovement;

    //This boolean tells whether the slime should be small
    //right now or not (for drains/holes)
    private boolean small = false;


    public JumpPlayer(LoadedLevel level) {
        super(2, 1, 1);
        this.position.set(level.getSpawnX(), level.getSpawnY());
        this.playerState = new SimpleStateMachine<>(Idle.class,
                Idle.class, Move.class, Respawn.class);
        System.out.println("New player created " + System.currentTimeMillis());
    }

    @Override
    public void update(float dTime) {
        super.update(dTime);
        playerState.update(dTime);

        SlimeGame.getGame().getLevelHandler().getCurrentLevel().getGrid().activateAllTiles(this);
    }

    public void kill() {
        if (!playerState.getCurrent().getClass().equals(Respawn.class)) {
            playerState.changeStateAfterUpdate(Respawn.class);
        }
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
