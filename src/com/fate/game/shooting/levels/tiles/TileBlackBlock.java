package com.fate.game.shooting.levels.tiles;

import com.fate.engine.graphics.Texture;
import com.fate.engine.tiles.Tile;

public class TileBlackBlock extends Tile {
	public TileBlackBlock(int x, int y, float tileSize) {
		super(x, y, tileSize, new Texture("textures/blackBlock.png"), "Black Block");
	}
}
