package com.ktar5.gameengine.statemachine;

import com.badlogic.gdx.utils.ObjectMap;
import com.ktar5.gameengine.Feature;
import com.ktar5.gameengine.util.Updatable;
import lombok.Getter;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;

public class SimpleStateMachine<T extends State> extends ObjectMap<Class<? extends T>, T> implements Updatable {
    @Getter
    private T current;
    private Class<? extends T> newState;

    @SafeVarargs
    public SimpleStateMachine(Class<? extends T> initial, Class<? extends T>... stateClasses) {
        super(stateClasses.length);
        for (Class<? extends T> stateClass : stateClasses) {
            try {
                this.put(stateClass, stateClass.getDeclaredConstructor().newInstance());
                Logger.debug("Registered state class: " + stateClass.getName());
                if (stateClass == initial) {
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
        current.start();
    }

    public SimpleStateMachine(T initial, T... states) {
        super(states.length);
        current = initial;
        this.put((Class<? extends T>) initial.getClass(), initial);
        Logger.debug("Registered state class: " + initial.getClass().getName());
        for (T state : states) {
            this.put((Class<? extends T>) state.getClass(), state);
            Logger.debug("Registered state class: " + state.getClass().getName());
        }
        current.start();
    }

    //TODO this is elegant but retarded
    boolean updating = false;

    @Override
    public void update(float dTime) {
        updating = true;
        current.update(dTime);
        if (newState != null) {
            changeState();
        }
        updating = false;
    }

    public void changeStateAfterUpdate(Class<? extends T> state) {
        newState = state;
        if(!updating){
            changeState();
        }
    }

    private void changeState() {
        current.end();
        if (Feature.LOG_STATE_MACHINE.isEnabled()) {
            Logger.debug("Changing state from " + current.getClass().getSimpleName() + " to " + newState.getSimpleName()
                    + " @ " + System.currentTimeMillis());
        }
        current = this.get(newState);
        this.get(newState).start();
        newState = null;
    }

}
