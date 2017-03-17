package com.morph.engine.graphics;

import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector3f;

public class Vertex {
	private Vector3f position;
	private Color color;
	private Vector2f texCoord;
	
	public Vertex(Vector3f position, Color color, Vector2f texCoord) {
		this.position = position;
		this.color = color;
		this.texCoord = texCoord;
	}
	
	public Vertex(Vector2f position, Color color, Vector2f texCoord) {
		this.position = new Vector3f(position, 0);
		this.color = color;
		this.texCoord = texCoord;
	}
	
	public Vertex(Vector3f position, Color color) {
		this(position, color, new Vector2f(0, 0));
	}
	
	public Vertex(Vector2f position, Color color) {
		this(new Vector3f(position, 0), color, new Vector2f(0, 0));
	}
	
	public Vertex(Vector3f position, Vector2f texCoord) {
		this(position, new Color(1, 1, 1), texCoord);
	}
	
	public Vertex(Vector2f position, Vector2f texCoord) {
		this(position, new Color(1, 1, 1), texCoord);
	}
	
	public Vertex(Vector3f position) {
		this(position, new Color(1, 1, 1), new Vector2f(0, 0));
	}
	
	public Vertex(Vector2f position) {
		this(position, new Color(1, 1, 1), new Vector2f(0, 0));
	}
	
	public String toString() {
		return "Vertex(" + position.toString() + ", " + color.toString() + ", " + texCoord.toString() + ")";
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Vector2f getTexCoord() {
		return texCoord;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setTexCoord(Vector2f texCoord) {
		this.texCoord = texCoord;
	}
}
