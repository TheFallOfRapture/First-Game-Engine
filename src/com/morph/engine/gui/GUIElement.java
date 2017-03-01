package com.morph.engine.gui;

import com.morph.engine.entities.Entity;
import com.morph.engine.math.Vector2f;

public class GUIElement extends Entity {
	protected Vector2f position;
	protected int depth;
	
	public GUIElement(Vector2f position, int depth) {
		this.position = position;
		this.depth = depth;
	}
	
	public GUIElement(Vector2f position) {
		this(position, 0);
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public int getDepth() {
		return depth;
	}
}
