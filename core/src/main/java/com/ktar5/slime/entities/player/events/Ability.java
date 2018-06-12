package com.ktar5.slime.entities.player.events;

import com.ktar5.slime.SlimeGame;
import com.ktar5.slime.engine.cooldown.CooldownType;
import com.ktar5.slime.engine.core.EngineManager;
import com.ktar5.slime.entities.player.JumpPlayer;
import lombok.Getter;

import java.util.UUID;

public abstract class Ability implements CooldownType {
    protected final UUID id;
    @Getter(lazy = true)
    private final JumpPlayer player = SlimeGame.getGame().getLevelHandler().getCurrentLevel().getPlayer();

    protected Ability() {
        this.id = UUID.randomUUID();
    }

    public abstract void start();

    public abstract void update(float dTime);

    protected abstract void end();

    public boolean isCooling() {
        return EngineManager.get().getCooldownManager().isCooling(getPlayer().getId(), this);
    }

    public void cooldown(int miliseconds) {
        EngineManager.get().getCooldownManager().addCooldown(getPlayer().getId(), this, miliseconds);
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
