package com.morph.engine.math;

public class Matrix3f {
	private float[] m;
	
	public Matrix3f(float...m) {
		this.m = m;
	}
	
	public String toString() {
		String result = "Matrix3f(";
		
		for (int i = 0; i < 8; i++) {
			result += m[i] + ", ";
			if (i % 3 == 2)
				result += "\n";
		}
		
		return result + m[8] + ")";
	}
	
	// TODO: BROKEN!
	public float getDeterminant() {
		float det = 0;
		
		for (int sign = 0; sign < 2; sign++) {
			for (int diagonal = 0; diagonal < 3; diagonal++) {
				float d = 1;
				for (int value = 0; value < 3; value++) {
					if (sign == 0) {
						d *= get((value + diagonal) % 3, value % 3);
					} else {
						d *= get(2 - ((value + diagonal) % 3), value % 3);
					}
				}
				
				if (sign == 0) {
					det += d;
				} else {
					det -= d;
				}
			}
		}
		
//		float det = 0;
//		
//		for (int i = 0; i < 3; i++) {
//			int sign = i % 2 == 0 ? 1 : -1;
//			det += sign * m[i] * minorDet(i, 0);
//		}
		
		return det;
	}
	
	public Matrix3f scale(float k) {
		float[] values = new float[9];
		for (int i = 0; i < 9; i++) {
			values[i] = m[i] * k;
		}
		
		return new Matrix3f(values);
	}
	
	public Matrix3f mulComp(Matrix3f mat) {
		float[] values = new float[9];
		for (int i = 0; i < 9; i++) {
			values[i] = m[i] * mat.get(i);
		}
		
		return new Matrix3f(values);
	}
	
	public float get(int i) {
		return m[i];
	}
	
	public float get(int i, int j) {
		return m[i + j * 3];
	}
	
	public float[] getValues() {
		return m;
	}
	
	public Matrix2f getMatrix2f(int a, int b, int c, int d) {
		return new Matrix2f(m[a], m[b], m[c], m[d]);
	}
	
	public Matrix3f getInverse() {
//		Matrix3f matOfMinors = new Matrix3f(
//				getMatrix2f(4, 5, 7, 8).getDeterminant(), getMatrix2f(3, 5, 6, 8).getDeterminant(), getMatrix2f(3, 4, 6, 7).getDeterminant(),
//				getMatrix2f(1, 2, 7, 8).getDeterminant(), getMatrix2f(0, 2, 6, 8).getDeterminant(), getMatrix2f(0, 1, 6, 7).getDeterminant(),
//				getMatrix2f(1, 2, 4, 5).getDeterminant(), getMatrix2f(0, 2, 3, 5).getDeterminant(), getMatrix2f(0, 1, 3, 4).getDeterminant()
//		);
		
		Matrix3f matOfMinors = new Matrix3f(
				minorDet(0, 0), minorDet(1, 0), minorDet(2, 0),
				minorDet(0, 1), minorDet(1, 1), minorDet(2, 1),
				minorDet(0, 2), minorDet(1, 2), minorDet(2, 2)
		);
		
		Matrix3f sign = new Matrix3f(
				1, -1, 1,
				-1, 1, -1,
				1, -1, 1
		);
		
		Matrix3f cofactor = matOfMinors.mulComp(sign);
		Matrix3f adjugate = cofactor.getTranspose();
		float invDet = 1f / getDeterminant();
		
		return adjugate.scale(invDet);
	}
	
	private Matrix2f minor(int i, int j) {
		float[] values = new float[4];
		int index = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (x != i && y != j) {
					values[index] = get(x, y);
					index++;
				}
				if (index == 4) {
					break;
				}
			}
		}
		
		return new Matrix2f(values);
	}
	
	private float minorDet(int i, int j) {
		return minor(i, j).getDeterminant();
	}

	private Matrix3f getTranspose() {
		return new Matrix3f(
				m[0], m[3], m[6],
				m[1], m[4], m[7],
				m[2], m[5], m[8]
		);
	}
}
