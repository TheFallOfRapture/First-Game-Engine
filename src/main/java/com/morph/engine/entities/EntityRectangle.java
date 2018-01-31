package com.morph.engine.entities;

import com.morph.engine.collision.components.BoundingBox2D;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.shaders.Shader;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.RenderDataUtils;

public class EntityRectangle extends Entity {
	protected float width;
	protected float height;
	protected Color color;
	
	public EntityRectangle(float x, float y, float width, float height, Color color, Shader<?> shader, Texture texture, boolean isTrigger) {
		this.width = width;
		this.height = height;
		this.color = color;
		
		Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);
		addComponent(new Transform2D(new Vector2f(x, y), new Vector2f(width, height)));
		addComponent(new BoundingBox2D(new Vector2f(x, y), halfSize, isTrigger));
		
		addComponent(RenderDataUtils.createSquare(color, shader, texture));
	}
	
	public EntityRectangle(Vector2f position, Vector2f size, Color color, Shader<?> shader, Texture texture, boolean isTrigger) {
		this.width = size.getX();
		this.height = size.getY();
		this.color = color;
		
		addComponent(new Transform2D(position, size));
		addComponent(new BoundingBox2D(position, size.scale(0.5f), isTrigger));
		
		addComponent(RenderDataUtils.createSquare(color, shader, texture));
	}
	
	public Vector2f getCenter() {
		Transform2D t2D = this.getComponent(Transform2D.class);
		return t2D.getPosition();
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
