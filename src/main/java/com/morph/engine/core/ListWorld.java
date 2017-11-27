package com.morph.engine.core;

import com.morph.engine.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 1/19/2017.
 */
public class ListWorld extends World {
    private List<Entity> entities;

    public ListWorld(Game game) {
        super(game);
        this.entities = new ArrayList<>();
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    public boolean addEntity(Entity e) {
        game.renderingEngine.register(e);
        return entities.add(e);
    }

    public void addEntities(List<? extends Entity> e) {
        entities.addAll(e);
        e.forEach(game.renderingEngine::register);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
        game.renderingEngine.unregister(e);
        e.destroy();
    }

    public void removeEntities(List<Entity> e) {
        entities.removeAll(e);
        e.forEach(en -> {
            game.renderingEngine.unregister(en);
            en.destroy();
        });
    }
}
