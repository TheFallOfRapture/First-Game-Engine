package com.morph.engine.core;

import com.morph.engine.entities.Entity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 1/19/2017.
 */
public interface IWorld {
    Game getGame();
    List<Entity> getEntities();

    default Entity getEntityByName(String name) {
        return getEntities().stream().collect(Collectors.toMap(Entity::getName, e -> e)).get(name);
    }

    default Entity getEntityByID(int id) {
        return getEntities().stream().collect(Collectors.toMap(Entity::getId, e -> e)).get(id);
    }

    boolean addEntity(Entity e); // TODO: Should this really be required?
    boolean removeEntity(Entity e);
}
