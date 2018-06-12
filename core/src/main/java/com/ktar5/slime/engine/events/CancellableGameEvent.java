package com.ktar5.slime.engine.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CancellableGameEvent extends GameEvent {
    private boolean cancelled = false;
}
