package com.ktar5.slime.utils.statemachine;

import com.badlogic.gdx.utils.ObjectMap;
import com.ktar5.slime.engine.util.Updatable;
import org.pmw.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;

public class SimpleStateMachine<T extends State> extends ObjectMap<Class<? extends T>, T> implements Updatable {
    private T current;
    private boolean stateChangeInProgress = false;
    
    @SafeVarargs
    public SimpleStateMachine(Class<? extends T> initial, Class<? extends T>... abilityClasses) {
        super(abilityClasses.length);
        for (Class<? extends T> abilityClass : abilityClasses) {
            try {
                this.put(abilityClass, abilityClass.getDeclaredConstructor().newInstance());
                Logger.debug("Registered ability class: " + abilityClass.getName());
                if (abilityClass == initial) {
                    current = this.get(initial);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (!this.containsKey(initial)) {
            throw new RuntimeException("You have forgotten to include the initial ability: "
                    + initial.getSimpleName() + " in your playerState list");
        }
    }
    
    @Override
    public void update(float dTime) {
        if (stateChangeInProgress) {
            System.out.println("State change is currently in progress so action has been cancelled");
            return;
        }
        current.update(dTime);
    }
    
    public void changeState(Class<? extends T> ability) {
        //Halt updating of current during
        stateChangeInProgress = true;
        
        current.end();
        current = this.get(ability);
        this.get(ability).start();
        
        //Resume updating of current
        stateChangeInProgress = false;
    }
    
}
