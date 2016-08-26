package com.fate.engine.tiles;

import java.util.ArrayList;
import java.util.List;

import com.fate.engine.core.Game;
import com.fate.engine.graphics.Shader;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;

public class Tilemap {
	private Tile[] tiles;
	private float tileSize;
	private int width, height;
	private Shader<?> shader;
	
	public Tilemap(Game game, float tileSize, int width, int height, Shader<?> shader) {
		this.tileSize = tileSize;
		this.width = width; 
		this.height = height;
		this.tiles = new Tile[width * height];
		this.shader = shader;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				TileEmpty empty = new TileEmpty(x, y, tileSize);
				empty.genRenderData(shader);
				tiles[x + y * width] = empty;
			}
		}
	}
	
	public Transform2D genTileTransform(int x, int y) {
		Transform2D t = new Transform2D(new Vector2f((x * tileSize) + (tileSize / 2f), (y * tileSize) + (tileSize / 2f)), 0, new Vector2f(tileSize, tileSize));
		return t;
	}
	
	public boolean canOccupyTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return true;
		
		return tiles[x + y * width].isEmpty();
	}
	
	public boolean canOccupyPosition(float x, float y) {
		int tilePosX = (int) (x / tileSize);
		int tilePosY = (int) (y / tileSize);
		
		return canOccupyTile(tilePosX, tilePosY);
	}
	
	public boolean canOccupyPosition(Vector2f v) {
		return canOccupyPosition(v.getX(), v.getY());
	}
	
	public float distanceXFromTile(int x, int y, Vector2f pos, Vector2f halfSize, int dir) {
		Tile tile = getTile(x, y);
		if (dir == 1) {
			return (x * tileSize) - pos.getX() - halfSize.getX();
		}
		
		return ((x+1) * tileSize) - pos.getX() + halfSize.getX();
	}
	
	public float distanceYFromTile(int x, int y, Vector2f pos, Vector2f halfSize, int dir) {
		Tile tile = getTile(x, y);
		if (dir == 1) {
			return (y * tileSize) - pos.getY() - halfSize.getY();
		}
		
		return ((y+1) * tileSize) - pos.getY() + halfSize.getY();
	}
	
	public float nearestTileDistanceInRow(Vector2f pos, Vector2f halfSize, int dir) {
		float dist = Float.POSITIVE_INFINITY;
		if (dir == 0) return dist;
		
		int tileX = (int) (pos.getX() / tileSize);
		int tileY = (int) (pos.getY() / tileSize);
		
		int end = dir == 1 ? width : 0;
		
		boolean found = false;
		
		if (tileY < 0 || tileY >= height) return dist;
		
		if (tileX > width && dir == 1) return dist;
		if (tileX < 0 && dir == -1) return dist;
		
		while (!found) {
			if (tileX < 0 || tileX >= width) {
				tileX += dir;
				continue;
			}
			
			if (!tiles[tileX + tileY * width].isEmpty()) {
				Vector2f bottomLeft = new Vector2f(tileX, tileY).scale(tileSize);
				if (dir == 1) {
					dist = bottomLeft.getX() - pos.getX() - halfSize.getX();
				} else {
					dist = bottomLeft.getX() + tileSize - pos.getX() + halfSize.getX();
				}
				
				found = true;
			}
			
			tileX += dir;
			
			if ((tileX >= end && dir == 1) || (tileX < 0 && dir == -1))
				break;
		}
		
		return dist;
	}
	
	public float nearestTileDistanceInColumn(Vector2f pos, Vector2f halfSize, int dir) {
		float dist = Float.POSITIVE_INFINITY;
		if (dir == 0) return dist;
		
		int tileX = (int) (pos.getX() / tileSize);
		int tileY = (int) (pos.getY() / tileSize);
		
		int end = dir == 1 ? width - 1 : 0;
		
		if (tileX < 0 || tileX >= width) return dist;
		
		if (tileY > height && dir == 1) return dist;
		if (tileY < 0 && dir == -1) return dist;
		
		boolean found = false;
		
		while (!found) {
			if (tileY < 0 || tileY >= height) {
				tileY += dir;
				continue;
			}
			
			if (!tiles[tileX + tileY * width].isEmpty()) {
				Vector2f bottomLeft = new Vector2f(tileX, tileY).scale(tileSize);
				if (dir == 1) {
					dist = bottomLeft.getY() - pos.getY() - halfSize.getY();
				} else {
					dist = bottomLeft.getY() + tileSize - pos.getY() + halfSize.getY();
				}
				
				found = true;
			}
			
			tileY += dir;
			
			if ((tileY >= end && dir == 1) || (tileY < 0 && dir == -1))
				return dist;
		}
		
		return dist;
	}
	
	public float nearestTileDistance(Vector2f pos, Vector2f halfSize, Vector2f dir) {
		int tileX = (int) (pos.getX() / tileSize);
		int tileY = (int) (pos.getY() / tileSize);
		
		int dirX = (int) Math.signum(dir.getX());
		int dirY = (int) Math.signum(dir.getY());
		
		boolean found = false;
		float dist = Float.POSITIVE_INFINITY;
		
		if (dirX == 0 && dirY == 0)
			return Float.POSITIVE_INFINITY;
		
		// Top-down: y passes below 0
		// Bottom-up: y passes above height
		// Right-to-left: x passes below 0
		// Left-to-right: x passes above width
		
		if (tileX >= width && dirX == 0)
			return Float.POSITIVE_INFINITY;
		if (tileY >= height && dirY == 0)
			return Float.POSITIVE_INFINITY;
		if (tileX < 0 && dirX == 0)
			return Float.POSITIVE_INFINITY;
		if (tileY < 0 && dirY == 0)
			return Float.POSITIVE_INFINITY;
		
		while (!found) {
			if (tileX < 0) tileX = 0;
			if (tileX >= width) tileX = width - 1;
			if (tileY < 0) tileY = 0;
			if (tileY >= height) tileY = height - 1;
			
			if (!tiles[tileX + tileY * width].isEmpty()) {
				Vector2f bottomLeft = new Vector2f(tileX, tileY).scale(tileSize);
				if (dirX == 0) {
					if (dirY == 1) {
						dist = bottomLeft.getY() - pos.getY() - halfSize.getY();
					} else {
						dist = bottomLeft.getY() + tileSize - pos.getY() + halfSize.getY();
					}
				} else if (dirY == 0) {
					if (dirX == 1) {
						dist = bottomLeft.getX() - pos.getX() - halfSize.getX();
					} else {
						dist = bottomLeft.getX() + tileSize - pos.getX() + halfSize.getX();
					}
				}
				
				found = true;
			}
			
			tileX += dirX;
			tileY += dirY;
		}
		
		return dist;
	}
	
	public void setTile(int x, int y, Tile tile) {
		tiles[x + y * width] = tile;
		tile.genRenderData(shader);
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x + y * width];
	}
	
	public Tile getTile(int index) {
		return tiles[index];
	}
	
	public float getTileSize() {
		return tileSize;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public List<Tile> getTiles() {
		List<Tile> tiles = new ArrayList<Tile>();
		for (Tile t : this.tiles) {
			tiles.add(t);
		}
		
		return tiles;
	}
}
