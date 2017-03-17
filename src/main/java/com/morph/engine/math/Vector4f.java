package com.morph.engine.math;

public class Vector4f {
	private float x, y, z, w;
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Clones a Vector4f.
	 */
	public Vector4f(Vector4f v) {
		this(v.getX(), v.getY(), v.getZ(), v.getW());
	}
	
	/**
	 * Creates a new Vector4f from a Vector3f and a supplied z value.
	 * @param v Vector3f containing new x and y values.
	 */
	public Vector4f(Vector3f v, float w) {
		this(v.getX(), v.getY(), v.getZ(), w);
	}
	
	/**
	 * Creates a new Vector4f from a Vector3f by using the x and y values of the Vector3f.
	 * @param v Vector3f containing new x and y values.
	 */
	public Vector4f(Vector3f v) {
		this(v, 0);
	}
	
	public Vector4f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Vector4f(Vector2f xy, int z, int w) {
		this.x = xy.getX();
		this.y = xy.getY();
		this.z = z;
		this.w = w;
	}

	public String toString() {
		return "Vector4f(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
	
	public Vector4f add(Vector4f v) {
		return new Vector4f(x + v.getX(), y + v.getY(), z + v.getZ(), w + v.getW());
	}
	
	public Vector4f sub(Vector4f v) {
		return new Vector4f(x - v.getX(), y - v.getY(), z - v.getZ(), w - v.getW());
	}
	
	public Vector4f scale(float k) {
		return new Vector4f(x * k, y * k, z * k, w * k);
	}
	
	public Vector4f invScale(float k) {
		return new Vector4f(x / k, y / k, z / k, w / k);
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
	
	public float getW() {
		return w;
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
	
	public void setW(float w) {
		this.w = w;
	}
	
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(Vector4f v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.w = v.getW();
	}

	public Vector3f getXYZ() {
		return new Vector3f(x, y, z);
	}

	public Vector2f getXY() {
		return new Vector2f(x, y);
	}
}
