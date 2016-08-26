package com.fate.game.platformer;

import com.fate.engine.core.Game;
import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;

public class Player extends EntityRectangle {
	public Player(Game game, int x, int y, int width, int height, Shader shader) {
		super(x, y, width, height, new Color(1, 1, 1, 1), shader, null, false);
	}
}
