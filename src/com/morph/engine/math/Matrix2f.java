package com.fate.engine.math;

public class Matrix2f {
	private float[] m;
	
	public Matrix2f(float...m) {
		this.m = m;
	}
	
	public float getDeterminant() {
		return (m[0] * m[3]) - (m[1] * m[2]);
	}
	
	public Matrix2f scale(float k) {
		return new Matrix2f(m[0] * k, m[1] * k, m[2] * k, m[3] * k);
	}
	
	public float get(int i, int j) {
		return m[i + j * 2];
	}
	
	public float[] getValues() {
		return m;
	}
	
	public Matrix2f getInverse() {
		float invDet = 1f / getDeterminant();
		return this.scale(invDet);
	}
	
	public Matrix2f getAdjugate() {
		return new Matrix2f(
				m[3], m[1],
				m[2], m[0]
		);
	}
}
