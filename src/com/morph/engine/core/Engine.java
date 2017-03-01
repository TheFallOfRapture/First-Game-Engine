package com.fate.engine.core;

import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.graphics.Texture;
import com.fate.engine.gui.GUIContainer;
import com.fate.engine.gui.GUIPanel;
import com.fate.engine.gui.GUITextElement;
import com.fate.engine.input.Keyboard;
import com.fate.engine.math.MatrixUtils;
import com.fate.engine.math.Vector2f;

import org.lwjgl.glfw.GLFW;

public class Engine extends OpenGame {
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
		parentContainer.addElement(new GUITextElement("File", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(5, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Edit", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(45, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Source", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(85, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Refactor", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(145, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Navigate", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(215, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Search", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(285, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Project", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(345, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Run", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(405, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Window", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(445, height - menuBarHeight + 10)));
		parentContainer.addElement(new GUITextElement("Help", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(510, height - menuBarHeight + 10)));
		
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
		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_T)) {
			addEntity(new GUITextElement("LOLOLO", "C:/Windows/Fonts/Calibri.ttf", 43, new Vector2f(0, 0)));
		}
	}
}
