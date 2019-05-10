package com.ktar5.slime.misc;

import org.tinylog.Logger;

public abstract class Feature<T> {
    private final String name;
    private T value;

    public Feature(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Feature(String name, String value) {
        this.name = name;
        this.value = deserializeValue(value);
    }

    public void setValue(T newValue){
        value = newValue;
        Logger.debug("");
    }

    public abstract T deserializeValue(String serialized);

    public abstract String serializeValue();

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }
}
