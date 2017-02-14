package com.fate.engine.core;

import com.fate.engine.entities.Entity;

/**
 * Created by Fernando on 1/19/2017.
 */
public abstract class OpenGame extends Game {
    public OpenGame(int width, int height, String title, float fps, boolean fullscreen) {
        super(width, height, title, fps, fullscreen);
        this.world = new ListWorld(this);
    }

    public final ListWorld getWorld() {
        return (ListWorld) world;
    }

    public void addEntity(Entity e) {
        getWorld().addEntity(e);
        renderingEngine.register(e);
    }
}
