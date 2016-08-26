package com.fate.game.shooting.entities;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.engine.physics.components.Velocity2D;

public class TestEnemy extends EntityRectangle {
	public TestEnemy(float x, float y, float width, float height, Color color, Shader<?> shader) {
		super(x, y, width, height, color, shader, new Texture("textures/4Head.png"), false);
		addComponent(new Velocity2D());
	}
}
