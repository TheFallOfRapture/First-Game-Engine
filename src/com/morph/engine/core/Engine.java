package com.morph.engine.core;

import com.morph.engine.core.gui.EngineGUI;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.graphics.Texture;
import com.morph.engine.newgui.*;
import com.morph.engine.input.Keyboard;
import com.morph.engine.math.MatrixUtils;
import com.morph.engine.math.Vector2f;

import com.morph.engine.physics.components.Transform2D;
import org.lwjgl.glfw.GLFW;

public class Engine extends OpenGame {
	private Button testBtn1;
	private Button testBtn2;

	private GUI testGUI;

	private float time;

	public Engine(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Game Engine", fps, fullscreen);
	}

	@Override
	public void initGame() {
		renderingEngine.setClearColor(0, 0, 0, 0);
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -5, 5));

//		float menuBarHeight = 30;
//		float toolbarWidth = 250;
//		float bottomBarHeight = 150;

		testGUI = new EngineGUI(this, width, height);

//		testGUI.addElement(new TextElement("File", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(5, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Edit", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(45, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Source", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(85, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Refactor", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(145, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Navigate", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(215, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Search", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(285, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Project", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(345, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Run", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(405, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Window", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(445, height - menuBarHeight + 10)));
//		testGUI.addElement(new TextElement("Help", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(510, height - menuBarHeight + 10)));
//		testGUI.addElement(new Panel(new Vector2f(0, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x9E9E9E, 1), new Texture("textures/solid.png")));
//		testGUI.addElement(new Panel(new Vector2f(width - toolbarWidth, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x9E9E9E, 1), new Texture("textures/solid.png")));
//		testGUI.addElement(new Panel(new Vector2f(toolbarWidth, 0), new Vector2f(width - (toolbarWidth * 2), bottomBarHeight), new Color(0x757575, 1), new Texture("textures/solid.png")));
//		testGUI.addElement(new Panel(new Vector2f(0, height - menuBarHeight), new Vector2f(width, menuBarHeight), new Color(0x616161, 1), new Texture("textures/solid.png")));
//
//		testBtn1 = new Button("Test Button", "C:/Windows/Fonts/Calibri.ttf", 16, new Color(1, 1, 1), new Color(0.5f, 0.5f, 0.5f), new Texture("textures/friendlyBlueMan.png"), new Texture("textures/4Head.png"), new Transform2D(new Vector2f(500, 400), 0, new Vector2f(100, 50)), 0);
//		testBtn2 = new Button("Test Button 2", "C:/Windows/Fonts/Calibri.ttf", 16, new Color(1, 1, 1), new Color(0.5f, 0.5f, 0.5f), new Texture("textures/friendlyBlueMan.png"), new Texture("textures/4Head.png"), new Transform2D(new Vector2f(500, 600), 0, new Vector2f(100, 50)), 0);
//
//		testBtn1.setOnHover(() -> System.out.println("Button 1 hovered over!"));
//		testBtn2.setOnHover(() -> System.out.println("Button 2 hovered over!"));
//		testBtn1.setOnClick(() -> System.out.println("Button 1 clicked!"));
//		testBtn2.setOnClick(() -> System.out.println("Button 2 clicked!"));
//
//		testGUI.addElement(testBtn1);
//		testGUI.addElement(testBtn2);
//		addElement(new Panel(new Vector2f(0, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x36393E, 1), new Texture("textures/solid.png")));
//		addElement(new Panel(new Vector2f(width - toolbarWidth, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x36393E, 1), new Texture("textures/solid.png")));
//		addElement(new Panel(new Vector2f(toolbarWidth, 0), new Vector2f(width - (toolbarWidth * 2), bottomBarHeight), new Color(0x2E3136, 1), new Texture("textures/solid.png")));
//		addElement(new Panel(new Vector2f(0, height - menuBarHeight), new Vector2f(width, menuBarHeight), new Color(0x2E3136, 1), new Texture("textures/solid.png")));
		addGUI(testGUI);
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
//		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_T)) {
//			addElement(new TextElement("LOLOLO", "C:/Windows/Fonts/Calibri.ttf", 43, new Vector2f(0, 0)));
//		}

//		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
//			float inc = 0.1f;
//
//			time += inc;
//
//			float alpha = (float) (Math.sin(time) * 0.5f) + 0.5f;
//
//			testBtn1.getRenderData().setLerpFactor(alpha);
//			testBtn2.getRenderData().setLerpFactor(alpha);
//		}
	}
}
