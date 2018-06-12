package com.ktar5.slime.entities.player.events;

import com.badlogic.gdx.utils.ObjectMap;
import org.pmw.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;

public class Abilities extends ObjectMap<Class<? extends Ability>, Ability> {

    @SafeVarargs
    public Abilities(Class<? extends Ability>... abilityClasses) {
        super(abilityClasses.length);
        for (Class<? extends Ability> abilityClass : abilityClasses) {
            try {
                this.put(abilityClass, abilityClass.getDeclaredConstructor().newInstance());
                Logger.debug("Registered ability class: " + abilityClass.getName());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
