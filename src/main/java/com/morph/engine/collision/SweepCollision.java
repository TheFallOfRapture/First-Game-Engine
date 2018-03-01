package com.morph.engine.collision;

import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector2f;

public class SweepCollision {
	private Entity entity;
	private Entity hit;
	private CollisionData data;
	private Vector2f position;
	private float time;
	
	public SweepCollision(Entity entity, Entity hit, CollisionData data, Vector2f position, float time) {
		this.entity = entity;
		this.hit = hit;
		this.data = data;
		this.position = position;
		this.time = time;
	}

	public Entity getEntity() {
		return entity;
	}

	public Entity getHit() {
		return hit;
	}

	public CollisionData getCollisionData() {
		return data;
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getTime() {
		return time;
	}
}
