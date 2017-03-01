package com.fate.game.shooting.entities.powerups;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.game.shooting.entities.components.powerups.SpeedBoost;

public class SpeedBoostEntity extends EntityRectangle {
	public SpeedBoostEntity(float x, float y, Shader<?> shader) {
		super(x, y, 3, 3, new Color(1, 1, 1), shader, new Texture("textures/lightningSpeed.png"), true);
		addComponent(new SpeedBoost(2));
	}
}
