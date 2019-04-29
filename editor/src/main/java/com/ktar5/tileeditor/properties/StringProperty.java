package com.ktar5.tileeditor.properties;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class StringProperty extends Property {
    @Getter @Setter public String value;

    public StringProperty(String name, String value, ParentProperty parent) {
        super(name, parent);
        this.value = value;
    }

    public StringProperty(String name, ParentProperty parent) {
        this(name, "", parent);
    }

    public void setNameAndValue(String name, String value) {
        this.changeName(name);
        this.setValue(value);
    }

    @Override
    public void serialize(JSONObject parent) {
        parent.put(getName(), getValue());
    }

}
