package com.morph.engine.math;

import java.util.function.Function;

public class Vector2f {
	private float x, y;
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Clones a Vector2f.
	 */
	public Vector2f(Vector2f v) {
		this.x = v.getX();
		this.y = v.getY();
	}
	
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2f(double x, double y) {
		this.x = (float)x;
		this.y = (float)y;
	}

	public String toString() {
		return "Vector2f(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector2f) {
			Vector2f v = (Vector2f) o;
			float epsilon = 1e-8f;
			
			return Math.abs(x - v.getX()) < epsilon && Math.abs(y - v.getY()) < epsilon;
		}
		
		return false;
	}
	
	public Vector2f add(Vector2f v) {
		return new Vector2f(x + v.getX(), y + v.getY());
	}
	
	public Vector2f sub(Vector2f v) {
		return new Vector2f(x - v.getX(), y - v.getY());
	}
	
	public Vector2f mul(Vector2f v) {
		return new Vector2f(x * v.getX(), y * v.getY());
	}
	
	public Vector2f div(Vector2f v) {
		return new Vector2f(x / v.getX(), y / v.getY());
	}
	
	public Vector2f scale(float k) {
		return new Vector2f(x * k, y * k);
	}
	
	public Vector2f invScale(float k) {
		return new Vector2f(x / k, y / k);
	}
	
	public float getLength() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	public Vector2f normalize() {
		float length = getLength();
		return this.invScale(length);
	}
	
	public Vector2f abs() {
		return new Vector2f(Math.abs(x), Math.abs(y));
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector2f v) {
		this.x = v.getX();
		this.y = v.getY();
	}

	public Matrix4f asTranslationMatrix() {
		return new Matrix4f(
				1, 0, 0, x,
				0, 1, 0, y,
				0, 0, 1, 0,
				0, 0, 0, 1
		);
	}
	
	public Matrix4f asScaleMatrix() {
		return new Matrix4f(
				x, 0, 0, 0,
				0, y, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		);
	}

	public float cross(Vector2f v) {
		return x * v.getY() - y * v.getX();
	}

	public void clear() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2f negate() {
		return new Vector2f(-x, -y);
	}

	public float dot(Vector2f v) {
		return (x * v.getX()) + (y * v.getY());
	}

	public Vector2f map(Function<Float, Float> fun) {
		return new Vector2f(fun.apply(x), fun.apply(y));
	}

	public static Vector2f reflect(Vector2f n, Vector2f v) {
		return v.add(n.scale(-2 * v.dot(n)));
	}
}
