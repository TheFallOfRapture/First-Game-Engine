package com.morph.engine.core;

import com.morph.engine.core.gui.EngineGUI;
import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityFactory;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.graphics.shaders.TintShader;
import com.morph.engine.newgui.*;
import com.morph.engine.math.MatrixUtils;

import com.morph.engine.util.KotlinTestKt;

public class Engine extends OpenGame {
	private Button testBtn1;
	private Button testBtn2;

	private EngineGUI testGUI;

	private float time;

	public Engine(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Game Engine", fps, fullscreen);
	}

	@Override
	public void initGame() {
		renderingEngine.setClearColor(0, 0, 0, 0);
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -5, 5));

		testGUI = new EngineGUI(this, width, height);

		addGUI(testGUI);

		KotlinTestKt.printMsg("Hello, world! Kotlin 1.1.1 is working in Morph 0.5.0!");

		Entity player = EntityFactory.getCustomTintRectangle("player", 20, 20, new Color(0, 1, 0), new TintShader());
		addEntity(player);

		attachBehavior("scripts/TestBehavior.kts");
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
