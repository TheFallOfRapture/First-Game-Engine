package com.morph.engine.collision.components;

import com.morph.engine.entities.Component;
import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityRectangle;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;

public class BoundingBox2D extends Component {
	private Vector2f center;
	private Vector2f halfSize;
	private boolean isTrigger;
	
	public BoundingBox2D(Vector2f center, Vector2f halfSize, boolean isTrigger) {
		this.center = center;
		this.halfSize = halfSize;
		this.isTrigger = isTrigger;
	}
	
	public BoundingBox2D(Vector2f center, Vector2f halfSize) {
		this(center, halfSize, false);
	}

	public Vector2f getCenter() {
		return center;
	}

	public void setCenter(Vector2f center) {
		this.center = center;
	}

	public Vector2f getHalfSize() {
		return halfSize;
	}

	public void setHalfSize(Vector2f halfSize) {
		this.halfSize = halfSize;
	}
	
	public boolean intersects(BoundingBox2D other) {
		/*
		 * ---------
		 * |       |
		 * |  b1   |            b2.minX < b1.maxX
		 * |    ---|---         b2.minY < b1.maxY
		 * |____|__|  |
		 *      |  b2 |
		 *      |_____|
		 */
		
		Vector2f b1Min = this.getMin();
		Vector2f b1Max = this.getMax();
		Vector2f b2Min = other.getMin();
		Vector2f b2Max = other.getMax();
		
		return (b2Min.getX() < b1Max.getX()) && (b2Min.getY() < b1Max.getY())
				&& (b2Max.getX() > b1Min.getX()) && (b2Max.getY() > b1Min.getY());
	}
	
	public boolean encloses(BoundingBox2D other) {
		Vector2f b1Min = this.getMin();
		Vector2f b1Max = this.getMax();
		Vector2f b2Min = other.getMin();
		Vector2f b2Max = other.getMax();
		
		return b1Min.getX() < b2Min.getX() && b1Max.getX() > b2Max.getX()
				&& b1Min.getY() < b2Min.getY() && b1Max.getY() > b2Max.getY();
	}
	
	public boolean contains(Vector2f point) {
		Vector2f min = this.getMin();
		Vector2f max = this.getMax();
		
		return min.getX() < point.getX() && point.getX() < max.getX()
				&& min.getY() < point.getY() && point.getY() < max.getY();
	}
	
	public Vector2f getMin() {
		return center.sub(halfSize);
	}
	
	public Vector2f getMax() {
		return center.add(halfSize);
	}

	public void update() {
		Entity parent = getParent();
		if (parent != null && parent.hasComponent(Transform2D.class)) {
			if (parent instanceof EntityRectangle)
				center = ((EntityRectangle) parent).getCenter();
			else
				center = parent.getComponent(Transform2D.class).getPosition();
		}
			
	}
	
	public boolean isTrigger() {
		return isTrigger;
	}
	
	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}
	
	public BoundingBox2D clone() {
		return new BoundingBox2D(center, halfSize, isTrigger);
	}
}
