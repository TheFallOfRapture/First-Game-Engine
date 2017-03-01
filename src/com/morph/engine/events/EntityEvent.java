package com.morph.engine.events;

import com.morph.engine.entities.Entity;

public class EntityEvent extends Event {
	public EntityEvent(Entity e) {
		super(e);
	}
	
	public Entity getSource() {
		return (Entity) source;
	}
}
