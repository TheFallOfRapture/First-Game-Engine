package com.fate.game.shooting.systems;

import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.collision.components.TriggerComponent;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.events.EventDispatcher;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.components.RenderData;
import com.fate.engine.math.Vector2f;
import com.fate.engine.math.Vector3f;
import com.fate.engine.physics.components.Velocity2D;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.controller.KeyboardController;
import com.fate.game.shooting.entities.components.AttackProjectile;
import com.fate.game.shooting.entities.components.Block;
import com.fate.game.shooting.entities.components.Health;
import com.fate.game.shooting.entities.components.Mirror;
import com.fate.game.shooting.entities.components.powerups.PowerUp;
import com.fate.game.shooting.events.DamageEvent;

public class TriggerSystem extends GameSystem {
	public TriggerSystem(ShootingGame game) {
		super(game);
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(TriggerComponent.class);
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
	
	protected void update(Entity e) {
		if (!e.hasComponent(TriggerComponent.class)) return;
		
		TriggerComponent c = e.getComponent(TriggerComponent.class);
		
		if (c.getParent().hasComponent(KeyboardController.class) 
				&& c.getCollidedEntity().hasComponent(PowerUp.class)) {
			PowerUp powerUp = c.getCollidedEntity().getComponent(PowerUp.class);
			powerUp.onPickup(c.getParent());
			((ShootingGame)game).getWorld().removeEntity(c.getCollidedEntity());
		}

		if (c.getCollidedEntity().hasComponent(AttackProjectile.class) && !c.getParent().hasComponent(AttackProjectile.class) && !c.getParent().hasComponent(Mirror.class)) {
			AttackProjectile p = c.getCollidedEntity().getComponent(AttackProjectile.class);
			p.applyBehavior(e);

			if (e.hasComponent(Health.class)) {
				e.getComponent(Health.class).handleAttackEvent(new DamageEvent(c.getCollidedEntity(), e, p.getAttackData(), game));
			}

			((ShootingGame)game).getWorld().removeEntity(c.getCollidedEntity());
		}

		if (c.getCollidedEntity().hasComponent(AttackProjectile.class) && c.getParent().hasComponent(Mirror.class)) {
			Velocity2D v = c.getCollidedEntity().getComponent(Velocity2D.class);
			v.setVelocity(Vector2f.reflect(c.getNormal().getXY(), v.getVelocity()));
		}

		c.setHandled(true);
	}
	
	protected void postUpdate(Entity e) {
		TriggerComponent c = e.getComponent(TriggerComponent.class);
		if (c == null) return;
		
		if (c.isHandled()) {
			c.getParent().removeComponent(c);
		}
	}
}
