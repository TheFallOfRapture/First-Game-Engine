package com.morph.engine.core;

import com.morph.engine.core.gui.EngineGUI;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.components.light.PointLight;
import com.morph.engine.input.InputMapping;
import com.morph.engine.input.KeyActions;
import com.morph.engine.input.MouseActions;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector3f;
import com.morph.engine.util.KotlinTestKt;

import static org.lwjgl.glfw.GLFW.*;

public class Engine extends Game {
	private EngineGUI testGUI;
	private PointLight light1 = new PointLight(15f, new Color(0.5f, 1f, 0.5f), new Vector3f(5f, -5f, 0f));
	private PointLight light2 = new PointLight(15f, new Color(1f, 0.5f, 0f), new Vector3f(-2f, 0f, 0f));
	private PointLight light3 = new PointLight(15f, new Color(0f, 0.5f, 1f), new Vector3f(2f, 0f, 0f));
	private float time = 0f;

	public Engine(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Game Engine", fps, fullscreen);
	}

	@Override
	public void initGame() {
		setWorld(new EngineWorld(this));
		renderingEngine.setClearColor(0, 0, 0, 0);
		setCamera(new OrthoCam2D(new Vector2f(0, 0), 0f, 10f * ((float) width / height), 10f));

		renderingEngine.addLight(light1);
		renderingEngine.addLight(light2);
		renderingEngine.addLight(light3);

		testGUI = new EngineGUI(this, width, height);
		testGUI.init();

		addGUI(testGUI);

		KotlinTestKt.printMsg("Hello, world! Kotlin 1.1.1 is working in Morph 0.5.0!");

//		Entity a = EntityFactory.getCustomTintRectangle("a", 2, 2, new Color(1, 0.5f, 0.5f), new BasicLightShader());
//		Entity b = EntityFactory.getCustomTintRectangle("b", 2, 2, new Color(0.5f, 1, 0.5f), new BasicLightShader());
//		Entity c = EntityFactory.getCustomTintRectangle("c", 2, 2, new Color(0.5f, 0.5f, 1), new BasicLightShader());
//		a.getComponent(RenderData.class).setTexture(new Texture("textures/testNormalMap.png"), 1);
//		b.getComponent(RenderData.class).setTexture(new Texture("textures/testNormalMap.png"), 1);
//		c.getComponent(RenderData.class).setTexture(new Texture("textures/testNormalMap.png"), 1);
//
//		a.getComponent(Transform2D.class).translate(new Vector2f(0, 2));
//		b.getComponent(Transform2D.class).translate(new Vector2f(0, -2));
//
//		addEntity(a);
//		addEntity(b);
//		addEntity(c);

		InputMapping input = new InputMapping();

		OrthoCam2D camera = (OrthoCam2D) getCamera();

		float speed = 0.1f;
		input.mapKey(GLFW_KEY_W, KeyActions.DOWN, () -> camera.setPosition(camera.getPosition().add(new Vector2f(0, speed))));
		input.mapKey(GLFW_KEY_A, KeyActions.DOWN, () -> camera.setPosition(camera.getPosition().add(new Vector2f(-speed, 0))));
		input.mapKey(GLFW_KEY_S, KeyActions.DOWN, () -> camera.setPosition(camera.getPosition().add(new Vector2f(0, -speed))));
		input.mapKey(GLFW_KEY_D, KeyActions.DOWN, () -> camera.setPosition(camera.getPosition().add(new Vector2f(speed, 0))));

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
		float speed = 1.5f;
		float radius = 5f;
		float depth = 0.8f;

//		light1.setLocalPosition(new Vector3f((float) Math.cos(time * speed) * radius, (float) Math.sin(time * speed) * radius, depth));
//		light2.setLocalPosition(new Vector3f((float) Math.cos(time * speed + (2f * Math.PI / 3f)) * radius, (float) Math.sin(time * speed + (2f * Math.PI / 3f)) * radius, depth));
//		light3.setLocalPosition(new Vector3f((float) Math.cos(time * speed + (4f * Math.PI / 3f)) * radius, (float) Math.sin(time * speed + (4f * Math.PI / 3f)) * radius, depth));
		time += dt;

		((OrthoCam2D)getCamera()).setRotation(time);
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
