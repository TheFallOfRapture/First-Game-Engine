package com.fate.game.shooting.entities;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.engine.physics.components.Velocity2D;

public class PuzzlePlayer extends EntityRectangle {
	public PuzzlePlayer(float x, float y, float width, float height, Shader<?> shader) {
		super(x, y, width, height, new Color(0.5f, 1, 0.5f), shader, new Texture("textures/tile.png"), false);
		addComponent(new Velocity2D());
	}
}
