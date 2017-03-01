package com.fate.game.flappybird;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.RigidBody;

public class Player extends EntityRectangle {
	public Player(int x, int y, int width, int height, Color color, float mass, Shader<?> shader) {
		super(x, y, width, height, color, shader, null, false);
		addComponent(new RigidBody(mass));
	}
	
	/**
	 * Bounces vertically upward.
	 * @param dt the time step
	 */
	public void bounce(float j, float dt) {
		RigidBody rigidBody = getComponent(RigidBody.class);
		
		if (rigidBody.getVelocity().getY() < 0)
			rigidBody.applyImpulse(rigidBody.getVelocity().scale(-1 * rigidBody.getMass()), dt);
		
		rigidBody.applyImpulse(new Vector2f(0, j), dt);
	}
}
