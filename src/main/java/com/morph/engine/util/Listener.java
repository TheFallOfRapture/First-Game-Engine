package com.morph.engine.util;

import io.reactivex.Emitter;

public interface Listener<T> {
    void onNext(T t);
    void onError(Throwable t);
}
