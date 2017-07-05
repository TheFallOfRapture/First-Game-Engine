package com.morph.engine.script;

import com.morph.engine.core.Game;
import com.morph.engine.entities.Entity;

/**
 * Created on 7/3/2017.
 */
public abstract class GameBehavior {
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    // Scripting API

    // Exposes all Entity methods
    public Entity getEntityByName(String name) {
        return game.getWorld().getEntityByName(name);
    }

    public void init() {}
    public void preUpdate() {}
    public void update() {}
    public void fixedUpdate(float dt) {}
    public void postUpdate() {}
    public void destroy() {}
}
