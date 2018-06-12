package com.ktar5.slime.engine.fsm;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters3;

public abstract class State3<T, A1, A2, A3> implements State<T> {

    final TriggerWithParameters3<A1, A2, A3, T> paraTrigger;

    public State3(T trigger, StateMachineConfig<State<T>, T> configuration) {
        paraTrigger = configuration.setTriggerParameters(trigger,
                getClassFromParameter(1),
                getClassFromParameter(2),
                getClassFromParameter(3));
        configuration.configure(this)
                .onEntryFrom(paraTrigger, this::onEnter)
                .onExit(this::onExit);
    }

    public void onEnter(A1 a1, A2 a2, A3 a3) {

    }

    public void onExit() {

    }

}
