package com.morph.engine.tiles;

import com.morph.engine.collision.components.BoundingBox2D;
import com.morph.engine.entities.Entity;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Shader;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.RenderDataUtils;

public class Tile extends Entity {
	private int x, y;
	private Texture texture;
	private String name;
	private boolean empty;
	
	public Tile(int x, int y, float tileSize, Texture texture, String name, boolean empty) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.name = name;
		this.empty = empty;
		addComponent(new Transform2D(new Vector2f((x * tileSize) + (tileSize / 2f), (y * tileSize) + (tileSize / 2f)), 0, new Vector2f(tileSize, tileSize)));
		addComponent(new BoundingBox2D(new Vector2f(x, y), new Vector2f(tileSize / 2f, tileSize / 2f)));
	}
	
	public Tile(int x, int y, float tileSize, Texture texture, String name) {
		this(x, y, tileSize, texture, name, false);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public void genRenderData(Shader<?> shader) {
		addComponent(RenderDataUtils.createSquare(new Color(1, 1, 1), shader, texture));
	}
}
