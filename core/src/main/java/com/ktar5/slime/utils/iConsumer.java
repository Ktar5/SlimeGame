package com.ktar5.slime.utils;

public interface iConsumer<T> {
    
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);
}
