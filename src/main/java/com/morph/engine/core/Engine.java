package com.morph.engine.core;

import com.morph.engine.core.gui.EngineGUI;
import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityFactory;
import com.morph.engine.events.EventDispatcher;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.graphics.shaders.TintShader;
import com.morph.engine.input.Keyboard;
import com.morph.engine.math.MatrixUtils;

import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.script.ScriptContainer;
import com.morph.engine.util.KotlinTestKt;
import org.lwjgl.glfw.GLFW;

public class Engine extends OpenGame {
	private EngineGUI testGUI;

	public Engine(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Game Engine", fps, fullscreen);

		EventDispatcher.INSTANCE.addEventHandler(this);

		Keyboard.getStandardKeyEvents().filter(e -> e.getKey() == GLFW.GLFW_KEY_GRAVE_ACCENT).subscribe(e -> toggleConsole());
	}

	@Override
	public void initGame() {
		renderingEngine.setClearColor(0, 0, 0, 0);
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -5, 5));

		testGUI = new EngineGUI(this, width, height);
		testGUI.init();

		addGUI(testGUI);

		KotlinTestKt.printMsg("Hello, world! Kotlin 1.1.1 is working in Morph 0.5.0!");

		Entity player = EntityFactory.getCustomTintRectangle("player", 20, 20, new Color(0, 1, 0), new TintShader());
		player.getComponent(Transform2D.class).translate(new Vector2f(50, 50));
		ScriptContainer sc = new ScriptContainer(this);
		player.addComponent(sc);
		sc.addBehaviorAsync("EScript.kts");
		sc.addBehaviorAsync("TestPythonScript.py");

		addEntity(player);

		attachBehaviorAsync("TestBehavior.kts");
		attachBehaviorAsync("TestBehavior2.kts");
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
//		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_GRAVE_ACCENT)) {
//			toggleConsole();
//		}
	}
}
