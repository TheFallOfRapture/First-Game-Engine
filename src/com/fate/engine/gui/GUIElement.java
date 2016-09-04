package com.fate.engine.gui;

import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;

public class GUIElement extends Entity {
	private Vector2f position;
	
	public GUIElement(Vector2f position) {
		this.position = position;
	}
	
	public Vector2f getPosition() {
		return position;
	}
}
