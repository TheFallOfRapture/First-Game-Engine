package com.fate.game.shooting.levels;

import com.fate.engine.entities.Entity;
import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Texture;
import com.fate.game.shooting.entities.TestEnemy;
import com.fate.game.shooting.entities.powerups.JumpBoostEntity;
import com.fate.game.shooting.entities.powerups.SpeedBoostEntity;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;

public class FirstPrototypeLevel extends Level {
	public FirstPrototypeLevel() {
		Entity edge1 = new EntityRectangle(20, 0.5f, 38, 1, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/testBlock.png"), false);
		Entity edge2 = new EntityRectangle(20, 29.5f, 38, 1, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/testBlock.png"), false);
		Entity edge3 = new EntityRectangle(0.5f, 15, 1, 28, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/redBlock.png"), false);
		Entity edge4 = new EntityRectangle(39.5f, 15, 1, 28, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/redBlock.png"), false);
		Entity corner1 = new EntityRectangle(0.5f, 0.5f, 1, 1, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/greenBlock.png"), false);
		Entity corner2 = new EntityRectangle(0.5f, 29.5f, 1, 1, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/greenBlock.png"), false);
		Entity corner3 = new EntityRectangle(39.5f, 0.5f, 1, 1, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/greenBlock.png"), false);
		Entity corner4 = new EntityRectangle(39.5f, 29.5f, 1, 1, new Color(1, 1, 1), new BasicTexturedShader(), new Texture("textures/greenBlock.png"), false);
		
		addEntity(edge1);
		addEntity(edge2);
		addEntity(edge3);
		addEntity(edge4);
		addEntity(corner1);
		addEntity(corner2);
		addEntity(corner3);
		addEntity(corner4);
		
		Entity testEnemy1 = new TestEnemy(20, 6, 5, 5, new Color(1, 1, 1), new BasicTexturedShader());
		Entity testEnemy2 = new TestEnemy(20, 12, 5, 5, new Color(1, 1, 1), new BasicTexturedShader());
		Entity testEnemy3 = new TestEnemy(20, 18, 5, 5, new Color(1, 1, 1), new BasicTexturedShader());
		Entity testEnemy4 = new TestEnemy(20, 24, 5, 5, new Color(1, 1, 1), new BasicTexturedShader());
		
		addEntity(testEnemy1);
		addEntity(testEnemy2);
		addEntity(testEnemy3);
		addEntity(testEnemy4);
		
		addEntity(new SpeedBoostEntity(25, 5, new BasicTexturedShader()));
		addEntity(new JumpBoostEntity(5, 25, new BasicTexturedShader()));
	}
}
