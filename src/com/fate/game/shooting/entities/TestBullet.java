package com.fate.game.shooting.entities;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.engine.physics.components.Velocity2D;

public class TestBullet extends EntityRectangle {
	public TestBullet(float x, float y, Shader<?> shader) {
		super(x, y, 0.5f, 0.5f, new Color(1, 1, 1), shader, new Texture("textures/testTexture.png"), false);
		addComponent(new Velocity2D());
	}
}
