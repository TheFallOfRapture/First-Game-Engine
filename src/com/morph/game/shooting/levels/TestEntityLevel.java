package com.fate.game.shooting.levels;

import com.fate.engine.entities.Entity;
import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Texture;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;

public class TestEntityLevel extends Level {
	public TestEntityLevel() {
		super();
		
		Entity test = new EntityRectangle(20, 0.5f, 40, 10, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/greenBlock.png"), false);
		addEntity(test);
	}
}
