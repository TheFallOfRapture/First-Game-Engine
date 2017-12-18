package com.morph.engine.util;

import java.util.ArrayList;
import java.util.List;

public class Feed<T> {
    private List<Listener<T>> listeners = new ArrayList<>();

    public void register(Listener<T> listener) {
        listeners.add(listener);
    }

    public void onNext(T t) {
        listeners.forEach(listener -> listener.onNext(t));
    }

    public void onError(Throwable t) {
        listeners.forEach(listener -> listener.onError(t));
    }
}
