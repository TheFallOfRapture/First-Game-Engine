package com.fate.engine.physics.components;

import com.fate.engine.entities.Component;
import com.fate.engine.math.Matrix4f;
import com.fate.engine.math.Quaternion;
import com.fate.engine.math.Vector2f;

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
}
