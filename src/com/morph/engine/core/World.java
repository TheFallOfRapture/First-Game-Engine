package com.morph.engine.core;

import com.morph.engine.entities.Entity;

import java.util.List;

/**
 * Created by Fernando on 1/19/2017.
 */
public abstract class World {
    protected Game game;

    public World(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public abstract List<Entity> getEntities();
}
