package com.ktar5.slime.engine.entities.components;

import com.badlogic.gdx.utils.Pool;
import com.ktar5.slime.engine.entities.LivingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;

@AllArgsConstructor
@Getter
@Setter
public class Health implements Pool.Poolable {
    private int health, maxHealth;
    @ToStringExclude
    private LivingEntity holder;

    public void heal(int amount) {
        amount = Math.abs(amount);
        if (health + amount >= maxHealth) {
            health = maxHealth;
        } else {
            health += amount;
        }
    }

    public void harm(int amount, DamageCause damageCause) {
        amount = Math.abs(amount);
        if (health - amount <= 0) {
            health = 0;
            onDeath(damageCause);
        } else {
            health -= amount;
        }
    }

    @Override
    public void reset() {
        health = maxHealth;
        holder = null;
    }

    private void onDeath(DamageCause damageCause) {

    }

    private void onDamage(DamageCause damageCause, int amount) {

    }

    private void onHeal(int amount) {

    }

    public enum DamageCause {
        PLAYER,
        HAZARD,
        CLIP,
        CUSTOM
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
