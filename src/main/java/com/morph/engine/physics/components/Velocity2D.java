package com.morph.engine.physics.components;

import com.morph.engine.entities.Component;
import com.morph.engine.math.Vector2f;

public class Velocity2D extends Component {
	private Vector2f velocity;
	
	public Velocity2D(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	public Velocity2D(float x, float y) {
		this(new Vector2f(x, y));
	}
	
	public Velocity2D() {
		this(new Vector2f(0, 0));
	}

	@Override
	public Component clone() {
		return new Velocity2D(velocity);
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	public void addVelocity(Vector2f velocity) {
		this.velocity = this.velocity.add(velocity);
	}
	
	public Vector2f getTranslation(float dt) {
		return velocity.scale(dt);
	}
}
