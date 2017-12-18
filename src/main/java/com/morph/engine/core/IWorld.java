package com.morph.engine.core;

import com.morph.engine.entities.Entity;
import com.morph.engine.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 1/19/2017.
 */
public interface IWorld {
    Game getGame();
    List<Entity> getEntities();

    default Entity getEntityByName(String name) {
        return getEntities()
                .parallelStream()
                .map(e -> new Pair<>(e, e.getName()))
                .filter(pair -> pair.getSecond().equals(name))
                .collect(Collectors.toList())
                .get(0)
                .getFirst();
    }

    default Entity getEntityByID(int id) {
        return getEntities()
                .parallelStream()
                .map(e -> new Pair<>(e, e.getID()))
                .filter(pair -> pair.getSecond().equals(id))
                .collect(Collectors.toList())
                .get(0)
                .getFirst();
    }

    boolean addEntity(Entity e); // TODO: Should this really be required?
}
