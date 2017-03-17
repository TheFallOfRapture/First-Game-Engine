package com.morph.engine.math;

public class MatrixUtils {
	public static Matrix4f getOrthographicProjectionMatrix(float top, float bottom, float left, float right, float near, float far) {
		return new Matrix4f(
				2f / (right - left), 0, 0, -((right + left) / (right - left)),
				0, 2f / (top - bottom), 0, -((top + bottom) / (top - bottom)),
				0, 0, -2f / (far - near), -((far + near) / (far - near)),
				0, 0, 0, 1
		);
	}
}
