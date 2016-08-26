package com.fate.engine.graphics;

public class Fragment {
	private int x, y;
	private Color color;
	private float z;
	
	public Fragment(int x, int y, Color color, float z) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public float getZ() {
		return z;
	}
}
