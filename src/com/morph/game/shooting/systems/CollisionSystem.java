package com.fate.game.shooting.systems;

import java.util.List;

import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.collision.components.CollisionComponent;
import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Velocity2D;

public class CollisionSystem extends GameSystem {
	public CollisionSystem(Game game) {
		super(game);
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(CollisionComponent.class);
	}

	@Override
	public void initSystem() {}

	protected void fixedUpdate(Entity e, float dt) {
		List<CollisionComponent> collisions = e.getComponents(CollisionComponent.class);
		
		for (CollisionComponent c : collisions) {
			if (e.hasComponent(Velocity2D.class) && !e.getComponent(BoundingBox2D.class).isTrigger()) {
				Velocity2D v2D = e.getComponent(Velocity2D.class);
				
				Vector2f vel = v2D.getVelocity();
				Vector2f blockDir = c.getNormal().getXY().negate();
				Vector2f remove = blockDir.scale(blockDir.dot(vel)).scale(1.0f - c.getTime());
				
				Vector2f newVelocity = vel.sub(remove);
				
				v2D.setVelocity(newVelocity);
			}
			
			c.setHandled(true);
		}
	}
	
	protected void postUpdate(Entity e) {
		List<CollisionComponent> collisions = e.getComponents(CollisionComponent.class);
		
		for (CollisionComponent c : collisions) {
			if (c.isHandled()) c.getParent().removeComponent(c);
		}
	}
}
