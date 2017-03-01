package com.fate.game.shooting.entities.components.attacks;

import com.fate.engine.core.Game;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform;
import com.fate.engine.physics.components.Transform2D;
import com.fate.engine.physics.components.Velocity2D;
import com.fate.game.shooting.entities.components.AttackProjectile;
import com.fate.game.shooting.events.AttackEvent;

import java.util.function.Consumer;

/**
 * Created by Fernando on 1/9/2017.
 */
public abstract class ProjectileAttack extends Attack {
    protected Game game;
    protected Entity template;
    protected Consumer<Entity> behavior;
    protected float speed;

    public ProjectileAttack(AttackData data, Game game, Entity template, Consumer<Entity> behavior, float speed) {
        super(data);
        this.game = game;
        this.template = template;
        this.behavior = behavior;
        this.speed = speed;
    }

    protected final Entity createProjectile(Vector2f pos, Vector2f dir) {
        Entity projectile = new Entity(template);
        projectile.getComponent(Transform2D.class).setPosition(pos);

        projectile.addComponent(new Velocity2D(dir.scale(speed)));
        projectile.addComponent(new AttackProjectile(behavior, data));

        System.out.println(projectile.getComponent(Transform2D.class).getPosition());
        return projectile;
    }
}
