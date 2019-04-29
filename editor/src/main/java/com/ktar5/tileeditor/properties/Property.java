package com.ktar5.tileeditor.properties;

import jdk.nashorn.internal.ir.PropertyNode;
import org.json.JSONObject;

public abstract class Property {
    public PropertyNode node;
    public final ParentProperty parent;
    public String nameProperty;

    public Property(String name, ParentProperty parent) {
        this.parent = parent;
        this.nameProperty = name;
    }

    public String getPath() {
        StringBuilder path = new StringBuilder();
        Property node = this;
        while (node != null) {
            path.insert(0, ".");
            path.insert(0, node.getName());
            node = node.getParent();
        }
        path.deleteCharAt(path.length() -1);
        return path.toString();
    }

    public ParentProperty getParent() {
        return this.parent;
    }

    public String getName() {
        return nameProperty;
    }

    public final void changeName(String newName) {
        this.nameProperty= newName;
    }

    public abstract void serialize(JSONObject parent);

}
