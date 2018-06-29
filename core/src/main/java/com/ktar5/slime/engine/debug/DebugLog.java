package com.ktar5.slime.engine.debug;

import com.ktar5.slime.engine.Feature;
import org.pmw.tinylog.Logger;

import java.util.HashMap;

public final class DebugLog {
    private static final HashMap<String, Boolean> debugs = new HashMap<>();
    
    private DebugLog() {
    }
    
    private static boolean get(String key) {
        if(Feature.DEBUG.isDisabled()){
            return false;
        }
        key = key.toLowerCase();
        if (debugs.containsKey(key)) {
            return debugs.get(key);
        }
        debugs.put(key, false);
        return false;
    }
    
    public static void set(String key, boolean value) {
        key = key.toLowerCase();
        debugs.put(key, value);
    }
    
    public static boolean isEnabled(String key) {
        return get(key);
    }
    
    public static boolean isDisabled(String key) {
        return !get(key);
    }
    
    public static void disable(String key) {
        set(key, false);
    }
    
    public static void enable(String key) {
        set(key, true);
    }
    
    public static void log(String key, String message){
        if(isEnabled(key)){
            Logger.debug("[" + key + "] " + message);
        }
    }
    
    
}
