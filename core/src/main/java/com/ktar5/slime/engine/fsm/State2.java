package com.ktar5.slime.engine.fsm;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;

public abstract class State2<T, A1, A2> implements State<T> {

    final TriggerWithParameters2<A1, A2, T> paraTrigger;

    public State2(T trigger, StateMachineConfig<State<T>, T> configuration) {
        paraTrigger = configuration.setTriggerParameters(trigger,
                getClassFromParameter(1), getClassFromParameter(2));
        configuration.configure(this).onEntryFrom(paraTrigger, this::onEnter).onExit(this::onExit);
    }

    @Override
    public T getTrigger() {
        return paraTrigger.getTrigger();
    }

    public void onEnter(A1 a1, A2 a2) {

    }

    public void onExit() {

    }

}
