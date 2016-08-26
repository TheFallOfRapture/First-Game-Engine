package com.fate.game.shooting.systems;

import com.fate.engine.collision.components.TriggerComponent;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.controller.KeyboardController;
import com.fate.game.shooting.entities.components.powerups.PowerUp;

public class PowerUpTriggerSystem extends GameSystem {
	public PowerUpTriggerSystem(ShootingGame game) {
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
			game.removeEntity(c.getCollidedEntity());
			
			c.setHandled(true);
		}
	}
	
	protected void postUpdate(Entity e) {
		TriggerComponent c = e.getComponent(TriggerComponent.class);
		if (c == null) return;
		
		if (c.isHandled()) {
			c.getParent().removeComponent(c);
		}
	}
}
