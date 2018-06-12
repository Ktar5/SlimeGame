package com.ktar5.slime.engine.fsm;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface State<T> {
    T getTrigger();


    public default <Z> Class<Z> getClassFromParameter(int index) {
        try {
            Type sooper = getClass().getGenericSuperclass();
            Type t = ((ParameterizedType) sooper).getActualTypeArguments()[index];
            String name = t.toString().replace("class ", "");
            System.out.println(name);
            return (Class<Z>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new NullPointerException("The class was null");
    }
}
