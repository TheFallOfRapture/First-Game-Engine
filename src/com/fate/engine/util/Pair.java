package com.fate.engine.util;

/**
 * Created by Fernando on 10/12/2016.
 */
public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T t, U u) {
        this.first = t;
        this.second = u;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(T t) {
        this.first = t;
    }

    public void setSecond(U u) {
        this.second = u;
    }
}
