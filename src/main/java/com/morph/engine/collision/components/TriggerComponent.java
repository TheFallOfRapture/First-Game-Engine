package com.morph.engine.collision.components;

import com.morph.engine.entities.Component;
import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector3f;

public class TriggerComponent extends Component {
	private Entity other;
	private boolean handled;
	private Vector3f normal;

	public TriggerComponent(Entity other, Vector3f normal) {
		this.other = other;
		this.handled = false;
		this.normal = normal;
	}
	
	public Entity getCollidedEntity() {
		return other;
	}

	public Vector3f getNormal() {
		return normal;
	}
	
	public boolean isHandled() {
		return handled;
	}
	
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
}
