package com.fate.engine.core;

import java.util.ArrayList;
import java.util.List;

import com.fate.engine.collision.CollisionEngine;
import com.fate.engine.entities.Entity;
import com.fate.engine.events.EventDispatcher;
import com.fate.engine.graphics.GLDisplay;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.gui.GUIContainer;
import com.fate.engine.input.Keyboard;
import com.fate.engine.input.Mouse;
import com.fate.engine.physics.PhysicsEngine;

public abstract class Game implements Runnable {
	protected int width, height;
	protected String title;
	protected PhysicsEngine physicsEngine;
	protected boolean isRunning = false;
	protected List<Entity> entities;
	protected boolean fullscreen;
	
	protected List<GameSystem> systems;
	
	protected GLDisplay display;
	protected GLRenderingEngine renderingEngine;
	
	protected float dt;
	
	public Game(int width, int height, String title, float fps, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.dt = 1.0f / fps;
		physicsEngine = new PhysicsEngine();
		entities = new ArrayList<Entity>();
		systems = new ArrayList<GameSystem>();
		this.fullscreen = fullscreen;
		EventDispatcher.INSTANCE.addEventHandler(this);
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
	}

	private void postUpdate() {
		postGameUpdate();
		
		for (GameSystem gs : systems) {
			gs.postUpdate();
		}
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
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void addContainer(GUIContainer c) {
		entities.add(c);
		c.getElements().forEach(entities::add);
	}
	
	public void addEntities(List<? extends Entity> e) {
		entities.addAll(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
		e.destroy();
	}
	
	public void removeEntities(List<Entity> e) {
		entities.removeAll(e);
	}
	
	public void fixedUpdate(float dt) {
		fixedGameUpdate(dt);
		
		for (GameSystem gs : systems) {
			gs.fixedUpdate(dt);
		}
	}
	
	public abstract void initGame();
	public abstract void preGameUpdate();
	public abstract void fixedGameUpdate(float dt);
	public abstract void postGameUpdate();
	public abstract void handleInput();
	public abstract void render();
	
	public boolean isRunning() {
		return isRunning;
	}

	public List<Entity> getEntities() {
		return entities;
	}
}
