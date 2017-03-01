package com.fate.game.shooting.levels.tiles;

import com.fate.engine.graphics.Texture;
import com.fate.engine.tiles.Tile;

public class TileRedBlock extends Tile {

	public TileRedBlock(int x, int y, float tileSize) {
		super(x, y, tileSize, new Texture("textures/redBlock.png"), "TileRedBlock");
	}

}
