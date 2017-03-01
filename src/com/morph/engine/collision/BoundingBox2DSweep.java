package com.fate.engine.collision;

import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;

public class BoundingBox2DSweep {
	private Entity entity;
	private Vector2f velocity;
	private boolean moving;
	
	public BoundingBox2DSweep(Entity entity, Vector2f velocity, boolean moving) {
		this.entity = entity;
		this.velocity = velocity;
		this.moving = moving;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public BoundingBox2D getBoundingBox() {
		return entity.getComponent(BoundingBox2D.class);
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public boolean isMoving() {
		return moving;
	}
}
