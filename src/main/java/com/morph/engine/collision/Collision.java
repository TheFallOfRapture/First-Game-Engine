package com.morph.engine.collision;

import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector2f;

public class Collision {
	private Entity entity;
	private Entity hit;
	private Vector2f position;
	private Vector2f intersection;
	private Vector2f normal;
	private float time;
	
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
