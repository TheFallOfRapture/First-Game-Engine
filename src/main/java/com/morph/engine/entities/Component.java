package com.morph.engine.entities;

import com.morph.engine.events.Message;

public abstract class Component implements Cloneable {
	protected Entity parent;
	protected boolean initialized = false;
	protected boolean requestDestroy = false;

	public void processMessage(Message m) {}
	
	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public boolean hasRequestedDestroy() {
		return requestDestroy;
	}
	
	public void requestDestroy() {
		this.requestDestroy = true;
	}
	
	public void init() {}
	public void destroy() {}
	
	public abstract Component clone();
}
