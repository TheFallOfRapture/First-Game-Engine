package com.fate.game.flappybird;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;

public class Obstacle extends EntityRectangle {
	public Obstacle(float x, float y, float width, float height, Shader<?> shader) {
		super(x, y, width, height, new Color(0.133f, 1, 0.133f, 1), shader, null, false);
	}
}
