package com.morph.engine.util;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

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

    public void emit(FlowableEmitter<T> emitter) {
        Listener<T> listener = new Listener<T>() {
            @Override
            public void onNext(T t) {
                emitter.onNext(t);
            }

            @Override
            public void onError(Throwable t) {
                emitter.onError(t);
            }
        };
        register(listener);
    }
}
