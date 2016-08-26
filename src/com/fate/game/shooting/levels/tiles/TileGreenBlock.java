package com.fate.game.shooting.levels.tiles;

import com.fate.engine.graphics.Texture;
import com.fate.engine.tiles.Tile;

public class TileGreenBlock extends Tile {
	public TileGreenBlock(int x, int y, float tileSize) {
		super(x, y, tileSize, new Texture("textures/greenBlock.png"), "TileGreenBlock");
	}
}
