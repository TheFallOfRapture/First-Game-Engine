package com.fate.game.shooting.levels;

import com.fate.engine.core.Game;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;
import com.fate.game.shooting.levels.tiles.TileBlackBlock;

public class SecondTestLevel extends PlatformerLevel {
	public SecondTestLevel(Game game) {
		super(game, 1f, 40, 40, new BasicTexturedShader());
		
		int rowLength = 10;
		for (int i = 0; i < rowLength; i++) {
			setTile(new TileBlackBlock(i, 0, tilemap.getTileSize()));
			setTile(new TileBlackBlock(i + 20, 0, tilemap.getTileSize()));
		}
	}
}
