package com.ktar5.slime.entities.player;


import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.PlayerEntity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.entities.player.states.Move;
import com.ktar5.slime.entities.player.states.PlayerState;
import com.ktar5.slime.entities.player.states.Respawn;
import com.ktar5.slime.utils.statemachine.SimpleStateMachine;
import com.ktar5.slime.world.LoadedLevel;
import com.ktar5.utilities.common.constants.Direction;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringExclude;

@Getter
public class JumpPlayer extends PlayerEntity {
    @ToStringExclude
    protected final SimpleStateMachine<PlayerState> playerState;
    private Direction previousDirection = Direction.N;
    
    public JumpPlayer(LoadedLevel level) {
        super(2, 1, 1);
        this.position.set(level.getSpawnX(), level.getSpawnY());
        this.playerState = new SimpleStateMachine<>(Move.class, Respawn.class);
    }
    
    @Override
    public void update(float dTime) {
        super.update(dTime);
        playerState.update(dTime);
    }
    
    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE, EngineManager.get().getAnimationLoader()
                .getTextureAsAnimation(this.getDefaultAnimation()), width, height);
    }
    
    public void resetAnimation(String newAnimation, boolean addDirection) {
        if (addDirection) {
            newAnimation += previousDirection.animationDirection;
        }
        getEntityAnimator().resetAnimation(EngineManager.get().getAnimationLoader().getAnimation(newAnimation));
    }
    
    @Override
    protected String getDefaultAnimation() {
        return "textures/Player.png";
    }
}
