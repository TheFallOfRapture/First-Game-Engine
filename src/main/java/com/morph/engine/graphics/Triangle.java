package com.morph.engine.graphics;

public class Triangle {
	private Vertex a, b, c;
	
	public Triangle(Vertex a, Vertex b, Vertex c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Vertex getA() {
		return a;
	}
	
	public Vertex getB() {
		return b;
	}
	
	public Vertex getC() {
		return c;
	}
}
