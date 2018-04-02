package com.morph.engine.util

/**
 * Created by Fernando on 2/13/2017.
 */
class Timer(private var interval: Float, private var action: Runnable) {
    private var accumulator: Float = 0.toFloat()
    private var running: Boolean = false

    init {
        this.running = false
    }

    fun step(dt: Float) {
        if (accumulator >= interval && running) {
            action.run()
            accumulator = 0f
        }

        if (running)
            accumulator += dt
    }

    fun setInterval(interval: Float) {
        this.interval = interval
    }

    fun setAction(action: Runnable) {
        this.action = action
    }

    fun start() {
        running = true
    }

    fun stop() {
        running = false
    }

    fun restart() {
        running = true
        accumulator = 0f
    }
}
