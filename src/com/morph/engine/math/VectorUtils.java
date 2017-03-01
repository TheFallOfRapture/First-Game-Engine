package com.fate.engine.math;

public class VectorUtils {
	public static float triangleArea(Vector2f v1, Vector2f v2, Vector2f v3) {
		Vector2f p1 = v1.sub(v3);
		Vector2f p2 = v2.sub(v3);
		
		return Math.abs(p1.getX() * p2.getY() - p1.getY() * p2.getX()) / 2;
	}
}
