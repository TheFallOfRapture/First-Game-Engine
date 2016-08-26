package com.fate.game.shooting.systems;

import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.physics.components.Transform2D;
import com.fate.engine.physics.components.Velocity2D;

public class VelocitySystem extends GameSystem {
	public VelocitySystem(Game game) {
		super(game);
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponents(Velocity2D.class, Transform2D.class);
	}
	
	protected void fixedUpdate(Entity e, float dt) {
		Velocity2D vel2D = e.getComponent(Velocity2D.class);
		Transform2D t2D = e.getComponent(Transform2D.class);
		
		t2D.translate(vel2D.getVelocity().scale(dt));
	}

	@Override
	public void initSystem() {}
}
