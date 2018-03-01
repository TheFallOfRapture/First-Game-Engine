package com.morph.engine.collision;

import com.morph.engine.entities.Entity;

public class Collision {
	private final Entity entity;
	private final Entity hit;
	private final CollisionData data;
	
	public Collision(Entity entity, Entity hit, CollisionData data) {
		this.entity = entity;
		this.hit = hit;
		this.data = data;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public Entity getHit() {
		return hit;
	}

	public CollisionData getData() {
		return data;
	}
}
