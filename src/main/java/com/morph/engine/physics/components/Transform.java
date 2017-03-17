package com.morph.engine.physics.components;

import com.morph.engine.entities.Component;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Quaternion;

public abstract class Transform extends Component {
	public Matrix4f getTransformationMatrix() {
		return getScaleMatrix().mul(getRotationMatrix()).mul(getTranslationMatrix());
	}
	
	public abstract void translate(Matrix4f translation);
	public abstract void rotate(Quaternion rotation);
	public abstract void scale(Matrix4f scale);
	
	public abstract Matrix4f getTranslationMatrix();
	public abstract Matrix4f getRotationMatrix();
	public abstract Matrix4f getScaleMatrix();
	
	public abstract Transform clone();

	public Matrix4f transform(Transform transform) {
		return transform.getTransformationMatrix().mul(this.getTransformationMatrix());
	}
}
