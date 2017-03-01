package com.morph.engine.util;

/**
 * Created by Fernando on 2/13/2017.
 */
public class Timer {
    private float interval;
    private float accumulator;
    private Runnable action;
    private boolean running;

    public Timer(float interval, Runnable action) {
        this.interval = interval;
        this.action = action;
        this.running = false;
    }

    public void step(float dt) {
        if (accumulator >= interval && running) {
            action.run();
            accumulator = 0;
        }

        if (running)
            accumulator += dt;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }
}
