package com.fate.engine.collision.components;

import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector3f;

public class TriggerComponent extends Component {
	private Entity other;
	private boolean handled;
	private Vector3f normal;

	public TriggerComponent(Entity other, Vector3f normal) {
		this.other = other;
		this.handled = false;
		this.normal = normal;
	}

//	public TriggerComponent(Entity other) {
//		this.other = other;
//		this.handled = false;
//		this.normal = new Vector2f();
//	}
	
	public Entity getCollidedEntity() {
		return other;
	}

	public Vector3f getNormal() {
		return normal;
	}

	@Override
	public TriggerComponent clone() {
		return new TriggerComponent(other, normal);
	}
	
	public boolean isHandled() {
		return handled;
	}
	
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
}
