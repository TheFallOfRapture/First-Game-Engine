package com.fate.engine.collision.components;

import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;

public class TriggerComponent extends Component {
	private Entity other;
	private boolean handled;
	
	public TriggerComponent(Entity other) {
		this.other = other;
		this.handled = false;
	}
	
	public Entity getCollidedEntity() {
		return other;
	}

	@Override
	public TriggerComponent clone() {
		return new TriggerComponent(other);
	}
	
	public boolean isHandled() {
		return handled;
	}
	
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
}
