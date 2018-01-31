package com.morph.engine.collision;

import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector2f;

public class Collision {
	private final Entity entity;
	private final Entity hit;
	private final Vector2f position;
	private final Vector2f intersection;
	private final Vector2f normal;
	private final float time;
	
	public Collision(Entity entity, Entity hit, Vector2f position, Vector2f intersection, Vector2f normal, float time) {
		this.entity = entity;
		this.hit = hit;
		this.position = position;
		this.intersection = intersection;
		this.normal = normal;
		this.time = time;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public Entity getHit() {
		return hit;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getIntersection() {
		return intersection;
	}
	
	public Vector2f getNormal() {
		return normal;
	}
	
	public float getTime() {
		return time;
	}
}
