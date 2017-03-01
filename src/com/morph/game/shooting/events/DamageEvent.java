package com.fate.game.shooting.events;

import com.fate.engine.core.Game;
import com.fate.engine.entities.Entity;
import com.fate.engine.events.EntityEvent;
import com.fate.engine.events.Event;
import com.fate.game.shooting.entities.components.attacks.AttackData;

/**
 * Created by Fernando on 1/11/2017.
 */
public class DamageEvent extends EntityEvent {
    private Entity damaged;
    private AttackData data;
    private Game game;

    public DamageEvent(Entity source, Entity damaged, AttackData data, Game game) {
        super(source);
        this.damaged = damaged;
        this.data = data;
        this.game = game;
    }

    public Entity getDamagedEntity() {
        return damaged;
    }

    public AttackData getAttackData() {
        return data;
    }

    public Game getGame() {
        return game;
    }
}
