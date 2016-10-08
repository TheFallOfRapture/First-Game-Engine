package com.fate.game.flappybird;

import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.components.RenderData;

public class ScoreDetector extends EntityRectangle {
	public ScoreDetector(FlappyBirdGame game, float x, float y, float width, float height, Shader<?> shader) {
		super(x, y, width, height, new Color(0, 0, 0, 1), shader, null, true);
		this.clearComponents(RenderData.class);
	}
}
