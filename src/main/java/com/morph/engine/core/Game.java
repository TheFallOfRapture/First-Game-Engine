package com.morph.engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.morph.engine.core.gui.ConsoleGUI;
import com.morph.engine.script.debug.Console;
import com.morph.engine.graphics.GLDisplay;
import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.input.Mouse;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.MatrixUtils;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Container;
import com.morph.engine.newgui.Element;
import com.morph.engine.newgui.GUI;
import com.morph.engine.script.GameBehavior;
import com.morph.engine.script.ScriptSystem;
import com.morph.engine.util.Feed;
import com.morph.engine.util.Pair;
import com.morph.engine.util.ScriptUtils;
import io.reactivex.Observable;

public abstract class Game implements Runnable {
	protected int width, height;
	protected String title;
	protected boolean isRunning = false;
	protected boolean fullscreen;

	protected IWorld world;
	protected List<GameSystem> systems;

	protected HashMap<String, GameBehavior> behaviors;

	protected GLDisplay display;
	protected GLRenderingEngine renderingEngine;

	protected float dt;

	protected List<Element> guiElements;
	private List<GUI> guis;

	private ScriptSystem scriptSystem;
	private Console console;
	private ConsoleGUI consoleGUI;

	public static Matrix4f screenOrtho;

	public static final int VERSION_MAJOR = 0;
	public static final int VERSION_MINOR = 6;
	public static final int VERSION_PATCH = 0;

	public static final String VERSION_STRING;

	static {
		VERSION_STRING = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;
	}

	private long delta;

	private Feed<GameAction> gameActionFeed = new Feed<>();

	public enum GameAction {
		INIT, PRE_UPDATE, UPDATE, FIXED_UPDATE, POST_UPDATE, RENDER, CLOSE
	}

	private Observable<GameAction> events = Observable.create(gameActionFeed::emit);

	public Game(int width, int height, String title, float fps, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.dt = 1.0f / fps;
		systems = new ArrayList<>();
		guiElements = new ArrayList<>();
		guis = new ArrayList<>();
		behaviors = new HashMap<>();
		this.fullscreen = fullscreen;
		this.console = new Console(Console.ScriptType.KOTLIN, this);
		this.consoleGUI = new ConsoleGUI(this, console, width, height);

		Game.screenOrtho = getScreenOrtho();

//		System.setOut(Console.out);
	}

	public void start() {
		if (isRunning)
			return;

		isRunning = true;

		init();

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

			delta = newTime - currentTime;

			accumulator += frameTime;

			while (accumulator >= dt) {
				fixedUpdate(dt);
				accumulator -= dt;
			}

			render();
			postUpdate();
		}

		destroy();
	}

	private void preUpdate() {
		gameActionFeed.onNext(GameAction.PRE_UPDATE);

		preGameUpdate();

		for (GameSystem gs : systems) {
			gs.preUpdate();
		}

		behaviors.values().forEach(GameBehavior::preUpdate);
	}

	private void postUpdate() {
		gameActionFeed.onNext(GameAction.POST_UPDATE);

		postGameUpdate();

		for (GameSystem gs : systems) {
			gs.postUpdate();
		}

		behaviors.values().forEach(GameBehavior::postUpdate);
	}

	protected void destroy() {
		display.destroy();
		ScriptUtils.stop();

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		gameActionFeed.onNext(GameAction.INIT);

		ScriptUtils.init(this);

		display = new GLDisplay(width, height, title);
		renderingEngine = new GLRenderingEngine(this);
		scriptSystem = new ScriptSystem(this);

		addSystem(renderingEngine);
		addSystem(scriptSystem);

		display.init();
		display.show();

		if (fullscreen)
			display.setFullscreen(width, height);

		initGame();

		systems.forEach(GameSystem::initSystem);

		consoleGUI.init();

		display.getEvents().filter(e -> e == GLDisplay.GLDisplayAction.CLOSE).subscribe(e -> handleExitEvent());

		// TODO: Oh my god please move this somewhere else
		Observable.zip(events.filter(e -> e == GameAction.UPDATE), Mouse.getStandardMouseEvents(), Pair::new).subscribe(pair -> {
			GameAction g = pair.getFirst();
			Mouse.StdMouseEvent m = pair.getSecond();

			Vector2f mousePos = Mouse.getScreenMousePosition();

			if (m.getButton() == 0 && m.getAction() == Mouse.StdMouseAction.PRESS)
				System.out.println("pressed");

			for (GUI gui : guis) {
				for (Element e : gui.getElements()) {
					switch (e.getState()) {
						case "IDLE":
							if (mousePos != null && e.contains(mousePos)) {
								if (m.getButton() == 0 && m.getAction() == Mouse.StdMouseAction.PRESS) {
									e.setState("CLICK");
								} else {
									e.setState("HOVER");
								}
							}
							break;
						case "HOVER":
							if (mousePos != null && e.contains(mousePos)) {
								if (m.getButton() == 0 && m.getAction() == Mouse.StdMouseAction.PRESS) {
									e.setState("CLICK");
								}
							} else {
								e.setState("IDLE");
							}
							break;
						case "CLICK":
							if (m.getButton() == 0 && m.getAction() == Mouse.StdMouseAction.RELEASE) {
								e.setState("IDLE");
							}
							break;
					}
				}
			}
		});
	}

	private void update() {
		gameActionFeed.onNext(GameAction.UPDATE);

		for (GameSystem gs : systems) {
			gs.update();
		}

//		if (Mouse.isMouseButtonPressed(0))
//			System.out.println("pressed");

		behaviors.values().forEach(GameBehavior::update);

//		for (GUI gui : guis) {
//			for (Element e : gui.getElements()) {
//				switch (e.getState()) {
//					case "IDLE":
//						if (mousePos != null && e.contains(mousePos)) {
//							if (Mouse.isMouseButtonPressed(0)) {
//								e.setState("CLICK");
//							} else {
//								e.setState("HOVER");
//							}
//						}
//						break;
//					case "HOVER":
//						if (mousePos != null && e.contains(mousePos)) {
//							if (Mouse.isMouseButtonPressed(0)) {
//								e.setState("CLICK");
//							}
//						} else {
//							e.setState("IDLE");
//						}
//						break;
//					case "CLICK":
//						if (!Mouse.isMouseButtonDown(0)) {
//							e.setState("IDLE");
//						}
//						break;
//				}
//			}
//		}
	}

	public void addSystem(GameSystem gs) {
		systems.add(gs);
	}

	public void removeSystem(GameSystem gs) {
		systems.remove(gs);
	}

	protected void pollEvents() {
		display.pollEvents();

//		if (display.isCloseRequested())
//			stop();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
		gameActionFeed.onNext(GameAction.CLOSE);
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

	public void removeElements(List<Element> e) {
		e.forEach(this::removeElement);
	}

	public void addGUI(GUI gui) {
		guis.add(gui);
		gui.load();
		addElements(gui.getElements());
		gui.open();
	}

	public void removeGUI(GUI gui) {
		guis.remove(gui);
		gui.unload();
		removeElements(gui.getElements());
		gui.close();
	}

	public void fixedUpdate(float dt) {
		gameActionFeed.onNext(GameAction.FIXED_UPDATE);

		fixedGameUpdate(dt);

		for (GameSystem gs : systems) {
			gs.fixedUpdate(dt);
		}

		guis.forEach(gui -> gui.fixedUpdate(dt));

		behaviors.values().forEach(b -> b.fixedUpdate(dt));
	}

	public void attachBehavior(String filename) {
		GameBehavior behavior = ScriptUtils.getScriptBehavior(filename);

		behavior.setGame(this);

		behaviors.put(filename, behavior);
		behavior.init();
		behavior.start();
	}

	public void attachBehaviorAsync(String filename) {
		ScriptUtils.getScriptBehaviorAsync(filename).subscribe(behavior -> {
			behavior.setGame(this);
			behaviors.put(filename, behavior);
			behavior.init();
			behavior.start();
		});
	}

	public void replaceBehavior(String filename, GameBehavior newBehavior) {
		System.out.println("Behavior " + filename + " has been modified.");
		newBehavior.setGame(this);
		behaviors.replace(filename, newBehavior);
		newBehavior.start();
	}

	public double getActualFPS() {
		return (1.0 / delta) * 1000000000.0;
	}

	public abstract void initGame();
	public abstract void preGameUpdate();
	public abstract void fixedGameUpdate(float dt);
	public abstract void postGameUpdate();
	public abstract void handleInput();

	public abstract IWorld getWorld();

	public final void render() {
		gameActionFeed.onNext(GameAction.RENDER);

		renderingEngine.render(display);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public Matrix4f getScreenOrtho() {
		return MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -1, 1);
	}

	public Console getConsole() {
		return console;
	}

	public void openConsole() {
		addGUI(consoleGUI);
	}

	public void closeConsole() {
		removeGUI(consoleGUI);
	}

	public void toggleConsole() {
		if (consoleGUI.isOpen()) closeConsole();
		else openConsole();
	}

	public boolean isConsoleOpen() {
		return consoleGUI.isOpen();
	}

	public void handleExitEvent() {
		System.out.println("Morph is closing...");
		stop();
	}

	public Observable<GameAction> getEvents() {
		return events;
	}
}
