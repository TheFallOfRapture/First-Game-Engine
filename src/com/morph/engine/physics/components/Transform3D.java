package com.morph.engine.physics.components;

import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Quaternion;
import com.morph.engine.math.Vector3f;

public class Transform3D extends Transform {
	private Vector3f position;
	private Quaternion orientation;
	private Vector3f scale;
	
	public Transform3D(Vector3f position, Quaternion orientation, Vector3f scale) {
		this.position = position;
		this.orientation = orientation;
		this.scale = scale;
	}
	
	public Transform3D(Vector3f position, Quaternion orientation) {
		this(position, orientation, new Vector3f(1, 1, 1));
	}
	
	public Transform3D(Vector3f position) {
		this(position, new Quaternion(new Vector3f(0, 1, 0), 0), new Vector3f(1, 1, 1));
	}
	
	public Transform3D(Vector3f position, Vector3f scale) {
		this(position, new Quaternion(new Vector3f(0, 1, 0), 0), scale);
	}

	@Override
	public Matrix4f getTranslationMatrix() {
		return position.asTranslationMatrix();
	}

	@Override
	public Matrix4f getRotationMatrix() {
		return orientation.asRotationMatrix();
	}

	@Override
	public Matrix4f getScaleMatrix() {
		return scale.asScaleMatrix();
	}

	@Override
	public void translate(Matrix4f translation) {
		position = position.add(translation.asTranslationVector());
	}

	@Override
	public void rotate(Quaternion rotation) {
		orientation = orientation.mul(rotation);
	}

	@Override
	public void scale(Matrix4f scaling) {
		scale = scale.add(scaling.asScaleVector());
	}
	
	public void translate(Vector3f translation) {
		position = position.add(translation);
	}

	public void scale(Vector3f scaling) {
		scale = scale.add(scaling);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public Transform3D clone() {
		return new Transform3D(position, orientation, scale);
	}
}
