package com.fate.game.flappybird;

import com.fate.engine.collision.components.TriggerComponent;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;

public class ScoreDetectorTriggerSystem extends GameSystem {
	public ScoreDetectorTriggerSystem(FlappyBirdGame game) {
		super(game);
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(TriggerComponent.class);
	}
	
	@Override
	public void initSystem() {}
	
	protected void fixedUpdate(Entity e, float dt) {
		TriggerComponent c = e.getComponent(TriggerComponent.class);
		
		if (c.isHandled()) c.getParent().removeComponent(c);
		
		if (c.getParent() instanceof Player && c.getCollidedEntity() instanceof ScoreDetector) {
			System.out.println("GOOD");
			((FlappyBirdGame)game).getWorld().removeEntity(c.getCollidedEntity());
			((FlappyBirdGame)game).incrementScore();
			c.setHandled(true);
		}
	}
}
