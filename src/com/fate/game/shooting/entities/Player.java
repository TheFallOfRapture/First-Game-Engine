package com.fate.game.shooting.entities;

import static org.lwjgl.glfw.GLFW.*;

import com.fate.engine.core.Game;
import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.engine.physics.components.Gravity;
import com.fate.engine.physics.components.Velocity2D;
import com.fate.game.shooting.controller.KeyboardController;
import com.fate.game.shooting.entities.components.Health;

public class Player extends EntityRectangle {
	public Player(int x, int y, int width, int height, Color color, Shader<?> shader) {
		super(x, y, width, height, color, shader, new Texture("textures/blueMan.png"), false);
		KeyboardController kc = new KeyboardController(15f, 0.5f, 0.8f, 20f, GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D);
		addComponent(kc);
		addComponent(new Velocity2D());
		addComponent(new Gravity());
		addComponent(new Health(100));
	}
}
