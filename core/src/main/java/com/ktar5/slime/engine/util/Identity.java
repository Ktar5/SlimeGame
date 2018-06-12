package com.ktar5.slime.engine.util;

import com.badlogic.gdx.utils.Pool;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class Identity implements Pool.Poolable{
    private UUID id;

    protected Identity() {
        id = UUID.randomUUID();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return equals(((Identity) o));
    }

    public boolean equals(Identity identity) {
        return identity.getId().equals(id);
    }

    @Override
    public void reset(){
        this.id = UUID.randomUUID();
    }

}
