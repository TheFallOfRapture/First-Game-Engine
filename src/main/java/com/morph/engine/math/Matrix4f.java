package com.morph.engine.math;

public class Matrix4f {
	private float m[];
	
	public Matrix4f() {
		m = new float[]{
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		};
	}
	
	public Matrix4f(float... values) {
		if (values.length == 16)
			this.m = values;
		else
			throw new RuntimeException("Illegal matrix array length");
	}

	public static Matrix4f identity() {
		return new Matrix4f(
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		);
	}

	public static Matrix4f empty() {
		return new Matrix4f(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	public String toString() {
		String result = "Matrix4f(";
		for (int i = 0; i < 15; i++) {
			result += m[i] + ", ";
			if (i % 4 == 3 && i != 0)
				result += "\n         ";
		}
		result += m[15] + ")";
		
		return result;
	}
	
	public float get(int i, int j) {
		return m[i + j * 4];
	}
	
	public void set(float value, int i, int j) {
		this.m[i + j * 4] = value;
	}
	
	public float[] getValues() {
		return m;
	}
	
	public Vector3f asTranslationVector() {
		return new Vector3f(m[3], m[7], m[11]);
	}
	
	public float as2DRotation() {
		float cos = m[0];
		float sin = m[4];
		return (float) Math.atan2(sin, cos);
	}
	
	public Vector3f asScaleVector() {
		return new Vector3f(m[0], m[5], m[10]);
	}
	
	public Matrix4f mul(Matrix4f other) {
		Matrix4f result = new Matrix4f();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.set(
						this.get(i, 0) * other.get(0, j) + 
						this.get(i, 1) * other.get(1, j) + 
						this.get(i, 2) * other.get(2, j) + 
						this.get(i, 3) * other.get(3, j)
						, i, j);
			}
		}
		
		return result;
	}
	
	public Vector4f mul(Vector4f v) {
		float newX = m[0] * v.getX() + m[1] * v.getY() + m[2] * v.getZ() + m[3] * v.getW();
		float newY = m[4] * v.getX() + m[5] * v.getY() + m[6] * v.getZ() + m[7] * v.getW();
		float newZ = m[8] * v.getX() + m[9] * v.getY() + m[10] * v.getZ() + m[11] * v.getW();
		float newW = m[12] * v.getX() + m[13] * v.getY() + m[14] * v.getZ() + m[15] * v.getW();
		
		return new Vector4f(newX, newY, newZ, newW);
	}
	
	public float[] asArray() {
		return m;
	}
	
	public Matrix4f getTranspose() {
		return new Matrix4f(
				m[0], m[4], m[8], m[12],
				m[1], m[5], m[9], m[13],
				m[2], m[6], m[10], m[14],
				m[3], m[7], m[11], m[15]
		);
	}

	public Vector2f mul(Vector2f v) {
		return mul(new Vector4f(v, 0, 0)).getXY();
	}
	
	public Vector3f mul(Vector3f v) {
		return mul(new Vector4f(v, 0)).getXYZ();
	}
	
	private Matrix3f minor(int i, int j) {
		float[] values = new float[9];
		int index = 0;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (x != i && y != j) {
					values[index] = get(x, y);
					index++;
				}
				if (index == 9) {
					break;
				}
			}
		}
		
		return new Matrix3f(values);
	}
	
	private float minorDet(int i, int j) {
		float minorDet = minor(i, j).getDeterminant();
		return minorDet;
	}
	
	public Matrix4f getInverse() {
		Matrix4f matOfMinors = new Matrix4f(
				minorDet(0, 0), minorDet(1, 0), minorDet(2, 0), minorDet(3, 0),
				minorDet(0, 1), minorDet(1, 1), minorDet(2, 1), minorDet(3, 1),
				minorDet(0, 2), minorDet(1, 2), minorDet(2, 2), minorDet(3, 2),
				minorDet(0, 3), minorDet(1, 3), minorDet(2, 3), minorDet(3, 3)
		);
		
		Matrix4f sign = new Matrix4f(
				1, -1, 1, -1,
				-1, 1, -1, 1,
				1, -1, 1, -1,
				-1, 1, -1, 1
		);
		
		Matrix4f cofactor = matOfMinors.mulComp(sign);
		Matrix4f adjugate = cofactor.getTranspose();
		float invDet = 1f / getDeterminant();
		
		Matrix4f inverse = adjugate.scale(invDet);
		
		return adjugate.scale(invDet);
	}

	public float getDeterminant() {
		float det = 0;
		
		for (int i = 0; i < 4; i++) {
			int sign = (i % 2 == 0) ? 1 : -1;
			det += sign * m[i] * minorDet(i, 0);
		}
		
		return det;
	}

	private Matrix4f scale(float k) {
		float[] values = new float[16];
		
		for (int i = 0; i < 16; i++) {
			values[i] = m[i] * k;
		}
		
		return new Matrix4f(values);
	}

	private Matrix4f mulComp(Matrix4f mat) {
		float[] values = new float[16];
		
		for (int i = 0; i < 16; i++) {
			values[i] = m[i] * mat.get(i);
		}
		
		return new Matrix4f(values);
	}

	private float get(int i) {
		return m[i];
	}
}
