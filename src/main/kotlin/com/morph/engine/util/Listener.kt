package com.morph.engine.util

interface Listener<in T> {
    fun onNext(t: T)
    fun onError(t: Throwable)
    fun onComplete()
}
