package com.fate.game.shooting.entities.components.attacks;

import com.fate.engine.core.Game;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.events.AttackEvent;

import java.util.function.Consumer;

/**
 * Created by Fernando on 1/11/2017.
 */
public class TargetedProjectileAttack extends ProjectileAttack {
    private Entity target;

    public TargetedProjectileAttack(AttackData data, Game game, Entity template, Entity target, Consumer<Entity> behavior, float speed) {
        super(data, game, template, behavior, speed);
        this.target = target;
    }

    @Override
    public void execute(AttackEvent e) {
        if (target.isDestroyed())
            return;

        Vector2f objectPos = e.getSource().getComponent(Transform2D.class).getPosition();

        Vector2f dir = target.getComponent(Transform2D.class).getPosition().sub(objectPos).normalize();

        float parentSize = e.getSource().getComponent(Transform2D.class).getScale().getLength();
        float templateSize = template.getComponent(Transform2D.class).getScale().getLength();
        float size = (parentSize + templateSize) * 0.5f;
        Vector2f projectilePos = objectPos.add(dir.scale(size));
        Entity projectile = createProjectile(projectilePos, dir);
        ((ShootingGame)game).getWorld().addEntity(projectile);
    }
}
