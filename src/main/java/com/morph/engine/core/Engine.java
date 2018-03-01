package com.morph.engine.core;

import com.morph.engine.core.gui.EngineGUI;
import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityFactory;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.components.light.PointLight;
import com.morph.engine.graphics.shaders.BasicLightShader;
import com.morph.engine.input.InputMapping;
import com.morph.engine.input.KeyActions;
import com.morph.engine.input.MouseActions;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.MatrixUtils;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector3f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.script.ScriptContainer;
import com.morph.engine.util.KotlinTestKt;

import static org.lwjgl.glfw.GLFW.*;

public class Engine extends OpenGame {
	private EngineGUI testGUI;

	public Engine(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Game Engine", fps, fullscreen);
	}

	@Override
	public void initGame() {
		renderingEngine.setClearColor(0, 0, 0, 0);
		setCamera(new OrthoCam2D(new Vector2f(0, 0), 20f, 20f));

		renderingEngine.addLight(new PointLight(10f, new Color(1f, 1f, 1f), new Vector3f(0f, 0f, 0f)));

		testGUI = new EngineGUI(this, width, height);
		testGUI.init();

		addGUI(testGUI);

		KotlinTestKt.printMsg("Hello, world! Kotlin 1.1.1 is working in Morph 0.5.0!");

		Entity player = EntityFactory.getCustomTintRectangle("player", 100, 100, new Color(0, 1, 0), new BasicLightShader());
		player.getComponent(Transform2D.class).translate(new Vector2f(50, 50));
		ScriptContainer sc = new ScriptContainer(this, player);

		player.addComponent(sc);
		sc.addBehaviorAsync("EScript.kts");
		sc.addBehaviorAsync("TestPythonScript.py");

		addEntity(player);

		attachBehaviorAsync("TestBehavior.kts");
		attachBehaviorAsync("TestBehavior2.kts");

		InputMapping input = new InputMapping();

		input.mapKey(GLFW_KEY_W, KeyActions.DOWN, () -> System.out.println("W"));
		input.mapKey(GLFW_KEY_A, KeyActions.DOWN, () -> System.out.println("A"));
		input.mapKey(GLFW_KEY_S, KeyActions.DOWN, () -> System.out.println("S"));
		input.mapKey(GLFW_KEY_D, KeyActions.DOWN, () -> System.out.println("D"));

		input.mapButton(GLFW_MOUSE_BUTTON_1, MouseActions.PRESS, () -> System.out.println("PRESS LEFT"));
		input.mapButton(GLFW_MOUSE_BUTTON_1, MouseActions.RELEASE, () -> System.out.println("RELEASE LEFT"));

		input.mapButton(GLFW_MOUSE_BUTTON_2, MouseActions.PRESS, () -> System.out.println("PRESS RIGHT"));
		input.mapButton(GLFW_MOUSE_BUTTON_2, MouseActions.RELEASE, () -> System.out.println("RELEASE RIGHT"));

		input.link(this);

		System.err.println("Game initialization complete.");
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

	@Override
	public Matrix4f getWorldProjection() {
		return MatrixUtils.INSTANCE.getOrthographicProjectionMatrix(height, 0, 0, width, -5, 5);
	}
}
