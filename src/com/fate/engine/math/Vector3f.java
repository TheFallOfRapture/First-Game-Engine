package com.fate.engine.math;

public class Vector3f {
	private float x, y, z;
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Clones a Vector3f.
	 */
	public Vector3f(Vector3f v) {
		this(v.getX(), v.getY(), v.getZ());
	}
	
	/**
	 * Creates a new Vector3f from a Vector2f and a supplied z value.
	 * @param v Vector2f containing new x and y values.
	 */
	public Vector3f(Vector2f v, float z) {
		this(v.getX(), v.getY(), z);
	}
	
	/**
	 * Creates a new Vector3f from a Vector2f by using the x and y values of the Vector2f.
	 * @param v Vector2f containing new x and y values.
	 */
	public Vector3f(Vector2f v) {
		this(v, 0);
	}
	
	public Vector3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public String toString() {
		return "Vector3f(" + x + ", " + y + ", " + z + ")";
	}
	
	public Matrix4f asTranslationMatrix() {
		return new Matrix4f(
				1, 0, 0, x,
				0, 1, 0, y,
				0, 0, 1, z,
				0, 0, 0, 1
		);
	}
	
	public Matrix4f asScaleMatrix() {
		return new Matrix4f(
				x, 0, 0, 0,
				0, y, 0, 0,
				0, 0, z, 0,
				0, 0, 0, 1
		);
	}
	
	public Vector3f add(Vector3f v) {
		return new Vector3f(x + v.getX(), y + v.getY(), z + v.getZ());
	}
	
	public Vector3f sub(Vector3f v) {
		return new Vector3f(x - v.getX(), y - v.getY(), z - v.getZ());
	}
	
	public Vector3f scale(float k) {
		return new Vector3f(x * k, y * k, z * k);
	}
	
	public Vector3f invScale(float k) {
		return new Vector3f(x / k, y / k, z / k);
	}
	
	public Vector3f cross(Vector3f v) {
		return new Vector3f(y * v.getZ() - z * v.getY(), z * v.getX() - x * v.getZ(), x * v.getY() - y * v.getX());
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vector3f v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}

	public Vector2f getXY() {
		return new Vector2f(x, y);
	}

	public float getLength() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}

	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}
}
