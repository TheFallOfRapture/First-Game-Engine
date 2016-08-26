package com.fate.game.shooting.levels;

import java.util.ArrayList;
import java.util.List;

import com.fate.engine.core.Game;
import com.fate.engine.entities.Entity;
import com.fate.engine.graphics.Shader;
import com.fate.engine.tiles.Tile;
import com.fate.engine.tiles.Tilemap;

public class PlatformerLevel {
	protected Tilemap tilemap;
	protected List<Entity> entities;
	protected int width, height;
	protected Shader<?> shader;
	
	public PlatformerLevel(Game game, float tileSize, int width, int height, Shader<?> shader) {
		this.width = width;
		this.height = height;
		this.tilemap = new Tilemap(game, tileSize, width, height, shader);
		this.entities = new ArrayList<Entity>();
		this.shader = shader;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Tilemap getTilemap() {
		return tilemap;
	}
	
	public Shader<?> getShader() {
		return shader;
	}
	
	public void setTile(Tile tile) {
		Tile oldTile = tilemap.getTile(tile.getX(), tile.getY());
		tilemap.setTile(tile.getX(), tile.getY(), tile);
		
		removeEntity(oldTile);
		addEntity(tile);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
}
