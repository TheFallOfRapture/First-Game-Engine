package com.morph.engine.math;

public class Quaternion {
	private float x, y, z, w;
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(Vector3f axis, float angle) {
		float cos = (float) Math.cos(angle / 2f);
		float sin = (float) Math.sin(angle / 2f);
		
		this.x = axis.getX() * sin;
		this.y = axis.getY() * sin;
		this.z = axis.getZ() * sin;
		this.w = cos;
	}
	
	public Quaternion mul(Quaternion other) {
		float newW = w * other.getW() - x * other.getX() - y * other.getY() - z * other.getZ();
		float newX = w * other.getX() + x * other.getW() + y * other.getZ() - z * other.getY();
		float newY = w * other.getY() - x * other.getZ() + y * other.getW() + z * other.getX();
		float newZ = w * other.getZ() + x * other.getY() - y * other.getX() + z * other.getW();
		
		return new Quaternion(newX, newY, newZ, newW);
	}
	
	public Matrix4f asRotationMatrix() {
		Matrix4f m1 = new Matrix4f(
				w, z, -y, x,
				-z, w, x, y,
				y, -x, w, z,
				-x, -y, -z, w
		);
		
		Matrix4f m2 = new Matrix4f(
				w, z, -y, -x,
				-z, w, x, -y,
				y, -x, w, -z,
				x, y, z, w
		);
		
		return m1.mul(m2);
	}
	
	public Vector4f toAxisAngle() {
		float axisX = (float) (x / Math.sqrt(1f - w*w));
		float axisY = (float) (y / Math.sqrt(1f - w*w));
		float axisZ = (float) (z / Math.sqrt(1f - w*w));
		float angle = (float) (2 * Math.acos(w));
		
		return new Vector4f(axisX, axisY, axisZ, angle);
	}
	
	public Quaternion normalize() {
		float length = getLength();
		float x = this.x / length;
		float y = this.y / length;
		float z = this.z / length;
		float w = this.w / length;
		
		return new Quaternion(x, y, z, w);
	}
	
	public float getLength() {
		return (float) Math.sqrt(x*x + y*y + z*z + w*w);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
}
