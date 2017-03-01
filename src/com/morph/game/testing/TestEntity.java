package com.fate.game.testing;

import com.fate.engine.core.Game;
import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;

public class TestEntity extends EntityRectangle {
	public TestEntity(Game game, float x, float y, float width, float height, Color color, Shader<?> shader) {
		super(x, y, width, height, color, shader, null, false);
	}
}
