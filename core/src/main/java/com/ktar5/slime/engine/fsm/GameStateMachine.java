package com.ktar5.slime.engine.fsm;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.delegates.Action2;
import com.github.oxo42.stateless4j.delegates.Action3;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters3;

public abstract class GameStateMachine<S, T> extends StateMachine<S, T> {


    public GameStateMachine(S intialState) {
        super(intialState);
    }

    public abstract T getTriggerFrom(S state);

    @Override
    public void publicFire(T trigger, Object... args) {
        super.publicFire(trigger, args);
    }

    protected void permit(S state, S... permits) {
        for (S permit : permits) {
            this.configure(state).permit(getTriggerFrom(permit), permit);
        }
    }

    public TriggerWithParameters<T> getTrigParam(T trigger) {
        return this.configuration().getTriggerConfiguration(trigger);
    }

    protected <A1> TriggerWithParameters1<A1, T> onEntryFromSelf(S state, Class<A1> arg1, Action1<A1> action) {
        if (this.configuration().getRepresentation(state) == null) {
            this.configuration().configure(state);
        }
        if (this.configuration().isTriggerConfigured(getTriggerFrom(state))) {
            throw new RuntimeException("Already configured trigger " + getTriggerFrom(state) + " during state " + state);
        }
        TriggerWithParameters1<A1, T> triggerParameters
                = this.configuration().setTriggerParameters(getTriggerFrom(state), arg1);
        this.configuration().configure(state).onEntryFrom(triggerParameters, action);
        return triggerParameters;
    }

    protected <A1, A2> TriggerWithParameters2<A1, A2, T> onEntryFromSelf(S state, Class<A1> arg1, Class<A2> arg2, Action2<A1, A2> action) {
        if (this.configuration().getRepresentation(state) == null) {
            this.configuration().configure(state);
        }
        if (this.configuration().isTriggerConfigured(getTriggerFrom(state))) {
            throw new RuntimeException("Already configured trigger " + getTriggerFrom(state) + " during state " + state);
        }
        TriggerWithParameters2<A1, A2, T> triggerParameters
                = this.configuration().setTriggerParameters(getTriggerFrom(state), arg1, arg2);
        this.configuration().configure(state).onEntryFrom(triggerParameters, action);
        return triggerParameters;
    }

    protected <A1, A2, A3> TriggerWithParameters3<A1, A2, A3, T> onEntryFromSelf(S state, Class<A1> arg1, Class<A2> arg2, Class<A3> arg3, Action3<A1, A2, A3> action) {
        if (this.configuration().getRepresentation(state) == null) {
            this.configuration().configure(state);
        }
        if (this.configuration().isTriggerConfigured(getTriggerFrom(state))) {
            throw new RuntimeException("Already configured trigger " + getTriggerFrom(state) + " during state " + state);
        }
        TriggerWithParameters3<A1, A2, A3, T> triggerParameters
                = this.configuration().setTriggerParameters(getTriggerFrom(state), arg1, arg2, arg3);
        this.configuration().configure(state).onEntryFrom(triggerParameters, action);
        return triggerParameters;
    }
}
