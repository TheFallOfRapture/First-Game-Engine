package com.fate.game.shooting.levels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fate.engine.entities.Entity;
import com.fate.engine.graphics.Color;
import com.fate.engine.math.Vector2f;
import com.fate.game.shooting.entities.components.Mirror;
import com.fate.game.shooting.levels.tiles.BlockEntity;

public class TestPuzzleLevel extends Level {
	private float xStart = 24.33f;
	private float yStart = 25f;
	
	private String[] levelData = new String[]{
			  "0 0 0 0 0 0 0 0 0 \n",
			  "0 0 0 0 0 0 0 0 0 \n",
			  "0 0 1 1 0 1 1 0 0 \n",
			  "0 0 0 0 0 0 0 0 0 \n",
			  "0 0 0 0 0 0 0 0 0 \n",
			  "0 0 0 0 0 0 0 0 0 \n",
			  "1 1 1 1 1 1 1 1 1 \n",
			  "1 1 0 0 0 0 0 1 1 \n",
			  "1 1 0 0 0 0 0 1 1 \n",
			  "1 1 0 0 0 0 0 1 1 \n",
			  "1 1 2 2 2 2 2 1 1 \n",
			  "1 1 0 0 0 0 0 1 1 \n",
			  "1 1 0 0 0 0 0 1 1 \n",
			  "1 1 1 1 1 1 1 1 1 \n"
	};
	
	public TestPuzzleLevel() {
		List<String> blocks = new ArrayList<String>();
		Arrays.stream(levelData).map(s -> s.split(" ")).forEach(s -> blocks.addAll(Arrays.asList(s)));
		float size = 1.75f;
		float x = xStart + size * 0.5f, y = yStart + size * 0.5f;
		for (String s : blocks) {
			if (!s.equals("\n")) {
				int id = Integer.parseInt(s);
				switch (id) {
					case 1:
						addEntity(new BlockEntity(new Vector2f(x, y), size, new Color(0.5f, 0.5f, 0.5f), false));
						break;
					case 2:
						Entity e = new BlockEntity(new Vector2f(x, y), size, new Color(0.5f, 0.5f, 1), false);
						e.addComponent(new Mirror());
						addEntity(e);
						break;
					default:
						break;
				}
				x += size;
			} else {
				x = xStart + size * 0.5f;
				y -= size;
			}
		}
	}
}
