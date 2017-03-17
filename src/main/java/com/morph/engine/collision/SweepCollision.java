package com.morph.engine.collision;

import com.morph.engine.math.Vector2f;

public class SweepCollision {
	private Collision collision;
	private Vector2f position;
	private float time;
	
	public SweepCollision(Collision collision, Vector2f position, float time) {
		this.collision = collision;
		this.position = position;
		this.time = time;
	}

	public Collision getCollision() {
		return collision;
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getTime() {
		return time;
	}
}
