package com.ktar5.slime.world.events;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodCall implements Runnable {
    private Method method;
    private String[][] methodCallParameters;

    public MethodCall(String string) {
        String name = string.substring(0, string.indexOf("("));
        String parens = string.substring(string.indexOf("(") + 1, string.indexOf(")"));
        String[] parameterSets = parens.split(";");
        methodCallParameters = new String[parameterSets.length][];
        for (int i = 0; i < parameterSets.length; i++) {
            methodCallParameters[i] = parameterSets[i].split(",");
        }

        Method[] methods = Events.class.getMethods();
        for (Method scan : methods) {
            if (scan.getName().equalsIgnoreCase(name)) {
                method = scan;
                break;
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void run() {
        try {
            for (String[] methodCallParameter : methodCallParameters) {
                Logger.debug(ToStringBuilder.reflectionToString(methodCallParameter, ToStringStyle.SIMPLE_STYLE));
                method.invoke(null, (Object[]) methodCallParameter);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
