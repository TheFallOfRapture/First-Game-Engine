package com.morph.engine.collision.components;

import com.morph.engine.entities.Component;
import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector3f;

public class CollisionComponent implements Component {
	private final Entity other;
	private final Vector3f normal;
	private final float distance;
	private final float time;
	private boolean handled;
	
	public CollisionComponent(Entity other, Vector3f normal, float distance, float time) {
		this.other = other;
		this.normal = normal;
		this.distance = distance;
		this.time = time;
		this.handled = false;
	}
	
	public Entity getCollidedEntity() {
		return other;
	}
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public float getTime() {
		return time;
	}
	
	public boolean isHandled() {
		return handled;
	}
	
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
}
