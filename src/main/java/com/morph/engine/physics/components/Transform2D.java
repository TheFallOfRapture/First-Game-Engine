package com.morph.engine.physics.components;

import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Quaternion;
import com.morph.engine.math.Vector2f;

public class Transform2D extends Transform {
	private Vector2f position;
	private float orientation;
	private Vector2f scale;
	
	public Transform2D(Vector2f position, float orientation, Vector2f scale) {
		this.position = position;
		this.orientation = orientation;
		this.scale = scale;
	}
	
	public Transform2D(Vector2f position, float orientation) {
		this(position, orientation, new Vector2f(1, 1));
	}
	
	public Transform2D(Vector2f position) {
		this(position, 0, new Vector2f(1, 1));
	}
	
	public Transform2D(Vector2f position, Vector2f scale) {
		this(position, 0, scale);
	}
	
	public Transform2D() {
		this(new Vector2f(0, 0), 0, new Vector2f(1, 1));
	}
	
	public String toString() {
		return "Transform2D(" + position + ", " + orientation + ", " + scale + ")";
	}

	@Override
	public Matrix4f getTranslationMatrix() {
		return position.asTranslationMatrix();
	}

	@Override
	public Matrix4f getRotationMatrix() {
		float cos = (float) Math.cos(orientation);
		float sin = (float) Math.sin(orientation);
		
		return new Matrix4f(
				cos, -sin, 0, 0,
				sin, cos, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		);
	}

	@Override
	public Matrix4f getScaleMatrix() {
		return scale.asScaleMatrix();
	}

	@Override
	public void translate(Matrix4f translation) {
		position = position.add(translation.asTranslationVector().getXy());
	}

	@Override
	public void rotate(Quaternion rotation) {
		orientation += rotation.toAxisAngle().getW();
	}

	@Override
	public void scale(Matrix4f scaling) {
		scale = scale.add(scaling.asScaleVector().getXy());
	}
	
	public void translate(Vector2f translation) {
		position = position.add(translation);
	}

	public void scale(Vector2f scaling) {
		scale = scale.add(scaling);
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public float getOrientation() {
		return orientation;
	}

	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
}
