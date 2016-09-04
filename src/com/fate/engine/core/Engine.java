package com.fate.engine.core;

import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.graphics.Texture;
import com.fate.engine.gui.GUIContainer;
import com.fate.engine.gui.GUIPanel;
import com.fate.engine.math.MatrixUtils;
import com.fate.engine.math.Vector2f;

public class Engine extends Game {
	private GUIContainer parentContainer;
	
	public Engine(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Game Engine", fps, fullscreen);
	}

	@Override
	public void initGame() {
		renderingEngine.setClearColor(0, 0, 0, 0);
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -5, 5));
		
		float menuBarHeight = 30;
		float toolbarWidth = 250;
		float bottomBarHeight = 150;
		
		parentContainer = new GUIContainer(new Vector2f(0, 0));
		parentContainer.addElement(new GUIPanel(new Vector2f(0, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x9E9E9E, 1), new Texture("textures/solid.png")));
		parentContainer.addElement(new GUIPanel(new Vector2f(width - toolbarWidth, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x9E9E9E, 1), new Texture("textures/solid.png")));
		parentContainer.addElement(new GUIPanel(new Vector2f(toolbarWidth, 0), new Vector2f(width - (toolbarWidth * 2), bottomBarHeight), new Color(0x757575, 1), new Texture("textures/solid.png")));
		parentContainer.addElement(new GUIPanel(new Vector2f(0, height - menuBarHeight), new Vector2f(width, menuBarHeight), new Color(0x616161, 1), new Texture("textures/solid.png")));
		
//		parentContainer.addElement(new GUIPanel(new Vector2f(0, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x36393E, 1), new Texture("textures/solid.png")));
//		parentContainer.addElement(new GUIPanel(new Vector2f(width - toolbarWidth, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x36393E, 1), new Texture("textures/solid.png")));
//		parentContainer.addElement(new GUIPanel(new Vector2f(toolbarWidth, 0), new Vector2f(width - (toolbarWidth * 2), bottomBarHeight), new Color(0x2E3136, 1), new Texture("textures/solid.png")));
//		parentContainer.addElement(new GUIPanel(new Vector2f(0, height - menuBarHeight), new Vector2f(width, menuBarHeight), new Color(0x2E3136, 1), new Texture("textures/solid.png")));
		
		addContainer(parentContainer);
	}

	@Override
	public void preGameUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fixedGameUpdate(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGameUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		renderingEngine.render(display, entities);
	}
}
