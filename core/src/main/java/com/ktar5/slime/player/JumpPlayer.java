package com.ktar5.slime.player;


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
import com.ktar5.slime.world.LoadedLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringExclude;

@Getter
public class JumpPlayer extends PlayerEntity {
    @ToStringExclude
    public final SimpleStateMachine<PlayerState> playerState;
    @Setter
    private Side lastMovedDirection = Side.UP;
    
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
    
    public void killPlayer() {
        playerState.changeStateAfterUpdate(Respawn.class);
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
