package com.fate.engine.collision.components;

import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector3f;

public class CollisionComponent extends Component {
	private Entity other;
	private Vector3f normal;
	private float distance;
	private float time;
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

	@Override
	public CollisionComponent clone() {
		return new CollisionComponent(other, normal, distance, time);
	}
	
	public boolean isHandled() {
		return handled;
	}
	
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
}
