package com.ktar5.slime.world.tiles.base;

import com.badlogic.gdx.maps.MapProperties;
import com.ktar5.slime.world.events.MethodCall;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

public abstract class TriggerableGameTile extends WholeGameTile {
    //onHit, onPass
    protected EnumMap<Trigger, List<MethodCall>> events;

    public TriggerableGameTile(int x, int y, Rotation rotation) {
        super(x, y, rotation);
        events = new EnumMap<>(Trigger.class);
    }

    @Override
    public void processProperty(MapProperties properties) {
        for (Iterator<String> iter = properties.getKeys(); iter.hasNext(); ) {
            String name = iter.next();
            String value = properties.get(name, String.class);
            addEvents(name, value);
        }
    }

    private void addEvents(String key, String value) {
        if (events == null) {
            events = new EnumMap<>(Trigger.class);
        }
        Trigger trigger = Trigger.fromString(key);
        if (trigger == null) {
            throw new IllegalArgumentException("The trigger: " + key + " is not valid at tile: " + x + ", " + y + ".");
        }
        List<MethodCall> actions = events.get(trigger);
        if (actions == null) {
            actions = new ArrayList<>();
            events.put(trigger, actions);
        }
        String[] split = value.split("\n");
        for (String s : split) {
            actions.add(new MethodCall(s));
        }
    }

    public void callEvent(Trigger trigger) {
        List<MethodCall> methodCalls = events.get(trigger);
        if (methodCalls != null) {
            for (MethodCall methodCall : methodCalls) {
                methodCall.run();
            }
        }
    }


    public enum Trigger {
        ON_HIT,
        ON_PASS;

        public static Trigger fromString(String value) {
            switch (value) {
                case "onHit":
                    return ON_HIT;
                case "onPass":
                    return ON_PASS;
            }
            return null;
        }
    }
}
