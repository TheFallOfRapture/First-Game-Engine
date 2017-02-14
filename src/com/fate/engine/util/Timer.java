package com.fate.engine.util;

/**
 * Created by Fernando on 2/13/2017.
 */
public class Timer {
    private float interval;
    private float accumulator;
    private Runnable action;

    public Timer(float interval, Runnable action) {
        this.interval = interval;
        this.action = action;
    }

    public void step(float dt) {
        if (accumulator >= interval) {
            action.run();
            accumulator = 0;
        }

        accumulator += dt;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
