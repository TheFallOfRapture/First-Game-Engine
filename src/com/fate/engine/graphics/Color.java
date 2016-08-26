package com.fate.engine.graphics;

public class Color {
	private float r, g, b, a;
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1);
	}
	
	public Color(int rgb, float alpha) {
		float red = ((rgb & 0xff0000) >> 16) / 256f;
		float green = ((rgb & 0x00ff00) >> 8) / 256f;
		float blue = (rgb & 0x0000ff) / 256f;
		this.r = red;
		this.g = green;
		this.b = blue;
		this.a = alpha;
	}
	
	public Color(int argb) {
		float alpha = ((argb & 0xff0000) >> 24) / 256f;
		float red = ((argb & 0xff0000) >> 16) / 256f;
		float green = ((argb & 0x00ff00) >> 8) / 256f;
		float blue = (argb & 0x0000ff) / 256f;
		this.r = red;
		this.g = green;
		this.b = blue;
		this.a = alpha;
	}
	
	public String toString() {
		return "Color(" + r + ", " + g + ", " + b + ", " + a + ")";
	}

	public float getRed() {
		return r;
	}
	
	public float getGreen() {
		return g;
	}
	
	public float getBlue() {
		return b;
	}
	
	public float getAlpha() {
		return a;
	}
	
	public void setRed(float red) {
		this.r = red;
	}
	
	public void setGreen(float green) {
		this.g = green;
	}
	
	public void setBlue(float blue) {
		this.b = blue;
	}
	
	public void setAlpha(float alpha) {
		this.a = alpha;
	}
	
	public Color scale(float k) {
		return new Color(r * k, g * k, b * k);
	}
	
	public Color add(Color c) {
		return new Color(r + c.getRed(), g + c.getGreen(), b + c.getBlue());
	}
	
	public Color clamp() {
		float red = (float) Math.max(0, Math.min(1, r));
		float green = (float) Math.max(0, Math.min(1, g));
		float blue = (float) Math.max(0, Math.min(1, b));
		
		return new Color(red, green, blue);
	}
	
	public int getARGBInteger() {
		int alpha = (int) Math.floor(a * 0xff);
		int red = (int) Math.floor(r * 0xff);
		int green = (int) Math.floor(g * 0xff);
		int blue = (int) Math.floor(b * 0xff);
		
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	
	public int getRGBInteger() {
		int red = (int) Math.floor(r * 0xff);
		int green = (int) Math.floor(g * 0xff);
		int blue = (int) Math.floor(b * 0xff);
		
		return (red << 16) | (green << 8) | blue;
	}
	
	public Color alphaBlend(Color dst) {
//		float outA = a + dst.getAlpha() * (1f-a);
		float red = ((r * a) + (dst.getRed() * (1f-a)));
		float green = ((g * a) + (dst.getGreen() * (1f-a)));
		float blue = ((b * a) + (dst.getBlue() * (1f-a)));
		
//		System.out.println(red + ", " + green + ", " + blue);
		
		return new Color(red, green, blue, 1);
	}
}
