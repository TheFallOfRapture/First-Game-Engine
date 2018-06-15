package com.morph.engine.core;

import com.morph.engine.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 1/19/2017.
 */
public abstract class ListWorld implements IWorld {
    private Game game;
    private List<Entity> entities;

    public ListWorld(Game game) {
        this.game = game;
        this.entities = new ArrayList<>();
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public Game getGame() {
        return game;
    }

    public boolean addEntity(Entity e) {
        game.renderingEngine.register(e);
        return entities.add(e);
    }

    public void addEntities(List<? extends Entity> e) {
        entities.addAll(e);
        e.forEach(game.renderingEngine::register);
    }

    public boolean removeEntity(Entity e) {
        game.renderingEngine.unregister(e);
        e.destroy();
        return entities.remove(e);
    }

    public void removeEntities(List<Entity> e) {
        entities.removeAll(e);
        e.forEach(en -> {
            game.renderingEngine.unregister(en);
            en.destroy();
        });
    }
}
