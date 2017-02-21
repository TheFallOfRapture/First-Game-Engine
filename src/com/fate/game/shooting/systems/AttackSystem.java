package com.fate.game.shooting.systems;

import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.game.shooting.entities.components.Attacker;
import com.fate.game.shooting.entities.components.attacks.Attack;

/**
 * Created by Fernando on 1/12/2017.
 */
public class AttackSystem extends GameSystem {
    public AttackSystem(Game game) {
        super(game);
    }

    @Override
    protected boolean acceptEntity(Entity e) {
        return e.hasComponent(Attacker.class);
    }

    @Override
    public void initSystem() {

    }
}
