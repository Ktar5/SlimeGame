package com.ktar5.slime.engine.debug;

import com.badlogic.gdx.utils.ObjectMap;
import org.pmw.tinylog.Logger;

public class Debug {
    private static final ObjectMap<Class, Boolean> debugMap = new ObjectMap<>();

    public static boolean shouldDebug(Class c){
        if (debugMap.containsKey(c)){
            return debugMap.get(c);
        }else{
            debugMap.put(c, true);
            return true;
        }
    }

    public static void setDebug(Class c, boolean debug){
        Logger.debug(c.getName() + " has had debug set to " + debug);
        debugMap.put(c, debug);
    }

}
