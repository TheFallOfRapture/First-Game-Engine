package com.fate.game.shooting.levels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fate.engine.graphics.Color;
import com.fate.engine.math.Vector2f;
import com.fate.game.shooting.levels.tiles.BlockEntity;

public class TestPuzzleLevel extends Level {
	private float xStart = 24.33f;
	private float yStart = 25f;
	
	private String[] levelData = new String[]{
			  "0 0 0 0 0 0 0 0 0 END",
			  "0 0 0 0 0 0 0 0 0 END",
			  "0 0 1 1 1 1 1 0 0 END",
			  "0 0 0 0 0 0 0 0 0 END",
			  "0 0 0 0 0 0 0 0 0 END",
			  "0 0 0 0 0 0 0 0 0 END",
			  "1 1 1 1 1 1 1 1 1 END",
			  "1 1 0 0 0 0 0 1 1 END",
			  "1 1 0 0 0 0 0 1 1 END",
			  "1 1 0 0 0 0 0 1 1 END",
			  "1 1 1 1 1 1 1 1 1 END"
	};
	
	public TestPuzzleLevel() {
		List<String> blocks = new ArrayList<String>();
		Arrays.asList(levelData).stream().map(s -> s.split(" ")).forEach(s -> blocks.addAll(Arrays.asList(s)));
		float size = 2;
		float x = xStart + size * 0.5f, y = yStart + size * 0.5f;
		for (String s : blocks) {
			if (!s.equals("END")) {
				int id = Integer.parseInt(s);
				if (id == 1) {
					addEntity(new BlockEntity(new Vector2f(x, y), size, new Color(0.5f, 0.5f, 0.5f), false));
				}
				x += size;
			} else {
				x = xStart + size * 0.5f;
				y -= size;
			}
		}
	}
}
