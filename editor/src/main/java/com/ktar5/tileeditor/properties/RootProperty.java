package com.ktar5.tileeditor.properties;

import org.json.JSONObject;

public class RootProperty extends ParentProperty {
    public RootProperty(JSONObject jsonObject) {
        super("properties", jsonObject, null);
    }
    
    public RootProperty() {
        super("properties", null);
    }
    
}
