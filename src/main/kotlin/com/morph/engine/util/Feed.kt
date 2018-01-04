package com.morph.engine.util

import io.reactivex.Emitter

class Feed<T> {
    private val listeners = ArrayList<Listener<T>>()

    fun register(listener: Listener<T>) {
        listeners.add(listener)
    }

    fun onNext(t: T) {
        listeners.forEach { listener -> listener.onNext(t) }
    }

    fun onError(t: Throwable) {
        listeners.forEach { listener -> listener.onError(t) }
    }

    fun onComplete() {
        listeners.forEach { it.onComplete() }
    }

    fun emit(emitter: Emitter<T>) {
        val listener = object : Listener<T> {
            override fun onNext(t: T) {
                emitter.onNext(t)
            }

            override fun onError(t: Throwable) {
                emitter.onError(t)
            }

            override fun onComplete() {
                emitter.onComplete()
            }
        }
        register(listener)
    }

    inline fun <reified R> emit(emitter: Emitter<R>, crossinline action: Emitter<R>.(T) -> Unit) {
        val listener = object : Listener<T> {
            override fun onNext(t: T) {
                emitter.action(t)
            }

            override fun onError(t: Throwable) {
                emitter.onError(t)
            }

            override fun onComplete() {
                emitter.onComplete()
            }
        }
        register(listener)
    }
}
