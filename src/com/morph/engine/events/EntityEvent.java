package com.fate.engine.events;

import com.fate.engine.entities.Entity;

public class EntityEvent extends Event {
	public EntityEvent(Entity e) {
		super(e);
	}
	
	public Entity getSource() {
		return (Entity) source;
	}
}
