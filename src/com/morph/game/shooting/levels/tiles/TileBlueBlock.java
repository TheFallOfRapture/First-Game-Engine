package com.fate.game.shooting.levels.tiles;

import com.fate.engine.graphics.Texture;
import com.fate.engine.tiles.Tile;

public class TileBlueBlock extends Tile {
	public TileBlueBlock(int x, int y, float tileSize) {
		super(x, y, tileSize, new Texture("textures/testBlock.png"), "TileBlueBlock", false);
	}
}
