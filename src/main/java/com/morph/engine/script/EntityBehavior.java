package com.morph.engine.script;

import com.morph.engine.entities.Entity;

/**
 * Created on 7/5/2017.
 */
public class EntityBehavior extends GameBehavior {
    private Entity self;

    public void setSelf(Entity e) {
        this.self = e;
    }

    public Entity getSelf() {
        return self;
    }
}
