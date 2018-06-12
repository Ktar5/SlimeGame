package com.ktar5.slime.engine.fsm;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

public abstract class State1<T, A1> implements State<T> {

    final TriggerWithParameters1<A1, T> paraTrigger;

    public State1(T trigger, StateMachineConfig<State<T>, T> configuration) {
        paraTrigger = configuration.setTriggerParameters(trigger, getClassFromParameter(1));
        configuration.configure(this).onEntryFrom(paraTrigger, this::onEnter).onExit(this::onExit);
    }

    @Override
    public T getTrigger() {
        return paraTrigger.getTrigger();
    }

    public void onEnter(A1 a1){

    }

    public void onExit(){

    }

}
