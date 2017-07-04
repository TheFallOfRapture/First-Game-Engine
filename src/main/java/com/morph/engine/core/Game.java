package com.morph.engine.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.morph.engine.entities.Entity;
import com.morph.engine.events.EventDispatcher;
import com.morph.engine.graphics.GLDisplay;
import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.input.Keyboard;
import com.morph.engine.input.Mouse;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.MatrixUtils;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Container;
import com.morph.engine.newgui.Element;
import com.morph.engine.newgui.GUI;
import com.morph.engine.physics.PhysicsEngine;
import com.morph.engine.script.GameBehavior;
import com.morph.engine.util.IOUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public abstract class Game implements Runnable {
	protected int width, height;
	protected String title;
	protected PhysicsEngine physicsEngine;
	protected boolean isRunning = false;
	protected boolean fullscreen;

	protected World world;
	protected List<GameSystem> systems;

	protected List<GameBehavior> behaviors;

	protected GLDisplay display;
	protected GLRenderingEngine renderingEngine;

	protected float dt;

	protected List<Element> guiElements;
	private List<GUI> guis;

	private ScriptEngine kotlinEngine;

	public static Matrix4f screenOrtho;

	public Game(int width, int height, String title, float fps, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.dt = 1.0f / fps;
		physicsEngine = new PhysicsEngine();
		systems = new ArrayList<>();
		guiElements = new ArrayList<>();
		guis = new ArrayList<>();
		behaviors = new ArrayList<>();
		this.fullscreen = fullscreen;
		EventDispatcher.INSTANCE.addEventHandler(this);

		Game.screenOrtho = getScreenOrtho();

		kotlinEngine = new ScriptEngineManager().getEngineByExtension("kts");
	}

	public void start() {
		if (isRunning)
			return;

		init();

		isRunning = true;
		long currentTime = System.nanoTime();
		double accumulator = 0.0;
		while (isRunning) {
			preUpdate();

			pollEvents();

			handleInput();
			update();

			long newTime = System.nanoTime();
			double frameTime = (newTime - currentTime) / 1000000000.0;
			currentTime = newTime;

			accumulator += frameTime;

			while (accumulator >= dt) {
				fixedUpdate(dt);
				accumulator -= dt;
			}

			render();
			postUpdate();
			Keyboard.clear();
			Mouse.clear();
		}

		destroy();
	}

	private void preUpdate() {
		preGameUpdate();

		for (GameSystem gs : systems) {
			gs.preUpdate();
		}

		behaviors.forEach(GameBehavior::preUpdate);
	}

	private void postUpdate() {
		postGameUpdate();

		for (GameSystem gs : systems) {
			gs.postUpdate();
		}

		behaviors.forEach(GameBehavior::postUpdate);
	}

	protected void destroy() {
		display.destroy();
	}

	private void init() {
		display = new GLDisplay(width, height, title);
		renderingEngine = new GLRenderingEngine(this);
		addSystem(renderingEngine);

		display.init();
		display.show();

		if (fullscreen)
			display.setFullscreen(width, height);

		initGame();

		systems.forEach(GameSystem::initSystem);
	}

	private void update() {
		for (GameSystem gs : systems) {
			gs.update();
		}

		Vector2f mousePos = Mouse.getScreenMousePosition();

		if (Mouse.isMouseButtonPressed(0))
			System.out.println("pressed");

		behaviors.forEach(GameBehavior::update);

		for (GUI gui : guis) {
			for (Element e : gui.getElements()) {
				switch (e.getState()) {
					case "IDLE":
						if (mousePos != null && e.contains(mousePos)) {
							if (Mouse.isMouseButtonPressed(0)) {
								e.setState("CLICK");
							} else {
								e.setState("HOVER");
							}
						}
						break;
					case "HOVER":
						if (mousePos != null && e.contains(mousePos)) {
							if (Mouse.isMouseButtonPressed(0)) {
								e.setState("CLICK");
							}
						} else {
							e.setState("IDLE");
						}
						break;
					case "CLICK":
						if (!Mouse.isMouseButtonDown(0)) {
							e.setState("IDLE");
						}
						break;
				}
			}
		}
	}

	public void addSystem(GameSystem gs) {
		systems.add(gs);
	}

	public void removeSystem(GameSystem gs) {
		systems.remove(gs);
	}

	protected void pollEvents() {
		display.pollEvents();

		if (display.isCloseRequested())
			stop();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}

	public void run() {
		start();
	}

	public void addElement(Element e) {
		guiElements.add(e);
		renderingEngine.register(e);
		if (e instanceof Container) {
			((Container)e).getChildren(true).forEach(this::addElement);
		}
	}

	public void removeElement(Element e) {
		guiElements.remove(e);
		renderingEngine.unregister(e);
		if (e instanceof Container) {
			((Container)e).getChildren(true).forEach(this::removeElement);
		}
	}

	public void addElements(List<Element> e) {
		e.forEach(this::addElement);
	}

	public void addGUI(GUI gui) {
		guis.add(gui);
		gui.load();
	}

	public void removeGUI(GUI gui) {
		guis.remove(gui);
		gui.unload();
		gui.getElements().forEach(this::removeElement);
	}

	public void fixedUpdate(float dt) {
		fixedGameUpdate(dt);

		for (GameSystem gs : systems) {
			gs.fixedUpdate(dt);
		}

		guis.forEach(gui -> gui.fixedUpdate(dt));

		behaviors.forEach(b -> b.fixedUpdate(dt));
	}

	public void attachBehavior(String filename) {
		String scriptSource = "";

		try {
			scriptSource = IOUtils.getFileAsString(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}

		GameBehavior behavior = null;

		try {
			behavior = (GameBehavior) kotlinEngine.eval(scriptSource);
			System.out.println(behavior.getClass().getSimpleName());
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		behaviors.add(behavior);
		behavior.init();
	}

	public abstract void initGame();
	public abstract void preGameUpdate();
	public abstract void fixedGameUpdate(float dt);
	public abstract void postGameUpdate();
	public abstract void handleInput();

	public abstract World getWorld();

	public final void render() {
		renderingEngine.render(display);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public List<Entity> getEntities() {
		return world.getEntities();
	}

	public Matrix4f getScreenOrtho() {
		return MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -1, 1);
	}
}
