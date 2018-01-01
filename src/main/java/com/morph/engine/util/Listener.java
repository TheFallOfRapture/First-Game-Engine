package com.morph.engine.util;

public interface Listener<T> {
    void onNext(T t);
    void onError(Throwable t);
    void onComplete();
}
