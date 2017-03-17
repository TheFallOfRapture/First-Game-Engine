package com.morph.engine.collision;

import com.morph.engine.collision.components.BoundingBox2D;
import com.morph.engine.math.Vector2f;

public class BoundingSweep {
	private BoundingBox2D box;
	private Vector2f delta;
	
	public BoundingSweep(BoundingBox2D box, Vector2f delta) {
		this.box = box;
		this.delta = delta;
	}

	public BoundingBox2D getBox() {
		return box;
	}

	public Vector2f getDelta() {
		return delta;
	}
}
