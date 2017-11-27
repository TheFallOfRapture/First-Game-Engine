package com.morph.engine.core;

import com.morph.engine.entities.Entity;
import com.morph.engine.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Entity getEntityByName(String name) {
        return getEntities()
                .parallelStream()
                .map(e -> new Pair<>(e, e.getName()))
                .filter(pair -> pair.getSecond().equals(name))
                .collect(Collectors.toCollection(ArrayList::new))
                .get(0)
                .getFirst();
    }

    public Entity getEntityByID(int id) {
        return getEntities()
                .parallelStream()
                .map(e -> new Pair<>(e, e.getID()))
                .filter(pair -> pair.getSecond().equals(id))
                .collect(Collectors.toCollection(ArrayList::new))
                .get(0)
                .getFirst();
    }

    public boolean addEntity(Entity e) {
        return getEntities().add(e);
    }
}
