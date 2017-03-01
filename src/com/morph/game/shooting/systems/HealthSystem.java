package com.fate.game.shooting.systems;

import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.entities.components.Health;

public class HealthSystem extends GameSystem {

	public HealthSystem(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponent(Health.class);
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
	
	protected void fixedUpdate(Entity e, float dt) {
		Health h = e.getComponent(Health.class);

		if (h.getCurrentHealth() <= 0) {
			((ShootingGame)game).getWorld().removeEntity(e);
		}
	}
}
