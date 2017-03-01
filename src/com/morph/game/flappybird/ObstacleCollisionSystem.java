package com.fate.game.flappybird;

import com.fate.engine.collision.components.CollisionComponent;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;

public class ObstacleCollisionSystem extends GameSystem {
	public ObstacleCollisionSystem(FlappyBirdGame game) {
		super(game);
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(CollisionComponent.class);
	}
	
	@Override
	public void initSystem() {}
	
	protected void update(Entity e) {
		CollisionComponent c = e.getComponent(CollisionComponent.class);
		
		if (c.getParent() instanceof Player && c.getCollidedEntity() instanceof Obstacle) {
			System.out.println("BAD");
			((FlappyBirdGame)game).envokeLoss();
			c.setHandled(true);
		}
	}
	
	protected void postUpdate(Entity e) {
		CollisionComponent c = e.getComponent(CollisionComponent.class);
		
		if (c.isHandled()) c.getParent().removeComponent(c);
	}
}
