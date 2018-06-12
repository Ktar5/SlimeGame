package com.ktar5.slime.entities.player;


import com.badlogic.gdx.math.Vector2;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.engine.entities.PlayerEntity;
import com.ktar5.slime.engine.entities.components.EntityAnimator;
import com.ktar5.slime.entities.player.events.Abilities;
import com.ktar5.slime.entities.player.events.Ability;
import com.ktar5.slime.entities.player.events.Move;
import com.ktar5.slime.entities.player.events.Respawn;
import com.ktar5.slime.world.LoadedLevel;
import com.ktar5.utilities.common.constants.Direction;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringExclude;

import static com.ktar5.slime.entities.player.JumpPlayerFSM.PlayerTrigger.MOVE;

@Getter
public class JumpPlayer extends PlayerEntity<JumpPlayerFSM.PlayerState, JumpPlayerFSM.PlayerTrigger> {
    @ToStringExclude
    protected final Abilities abilities;
    private Direction previousDirection = Direction.N;
    private boolean isTweening;

    public JumpPlayer(LoadedLevel level) {
        super(null, 2, 1, 1);
        this.position.set(level.getSpawnX(), level.getSpawnY());
        this.abilities = new Abilities(Move.class, Respawn.class);
        this.playerFSM = new JumpPlayerFSM(this);
    }

    @Override
    public void update(float dTime) {
        super.update(dTime);
        switch (playerFSM.getState().trigger) {
            case MOVE:
                if (!isTweening) {
                    getAbility(Move.class).update(dTime);
                }
                break;
            case IDLE:
                movement.update(dTime);
                if (!movement.getInput().equals(Vector2.Zero)) {
                    playerFSM.publicFire(MOVE, this);
                }
                break;
            case RESPAWN:
                break;
        }
    }
    
    @Override
    protected EntityAnimator initializeRenderer(float height, float width) {
        return new EntityAnimator(EntityAnimator.RenderLayer.MIDDLE, EngineManager.get().getAnimationLoader().getTextureAsAnimation(this.getDefaultAnimation()), width, height);
    }
    
    public <A extends Ability> Ability getAbility(Class<A> ability) {
        return this.getAbilities().get(ability);
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
