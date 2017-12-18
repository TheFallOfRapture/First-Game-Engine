package com.morph.engine.core;

import com.morph.engine.entities.Entity;

public abstract class GameSystem {
	protected Game game;
	
	public GameSystem(Game game) {
		this.game = game;
	}
	
	protected abstract boolean acceptEntity(Entity e);
	
	public abstract void initSystem();
	
	public final void preUpdate() {
		for (int i = game.getWorld().getEntities().size() - 1; i >= 0; i--) {
			Entity e = game.getWorld().getEntities().get(i);
			if (e != null && acceptEntity(e)) {
				preUpdate(e);
			}
		}
		
		systemPreUpdate();
	}
	
	public final void update() {
		for (int i = game.getWorld().getEntities().size() - 1; i >= 0; i--) {
			Entity e = game.getWorld().getEntities().get(i);
			if (e != null && acceptEntity(e)) {
				update(e);
			}
		}
		
		systemUpdate();
	}
	
	public final void fixedUpdate(float dt) {
		for (int i = game.getWorld().getEntities().size() - 1; i >= 0; i--) {
			Entity e = game.getWorld().getEntities().get(i);
			if (e != null && acceptEntity(e)) {
				fixedUpdate(e, dt);
			}
		}
		
		systemFixedUpdate(dt);
	}
	
	public final void postUpdate() {
		for (int i = game.getWorld().getEntities().size() - 1; i >= 0; i--) {
			Entity e = game.getWorld().getEntities().get(i);
			if (e != null && acceptEntity(e)) {
				postUpdate(e);
			}
		}
		
		systemPostUpdate();
	}
	
	protected void preUpdate(Entity e) {}
	protected void update(Entity e) {}
	protected void fixedUpdate(Entity e, float dt) {}
	protected void postUpdate(Entity e) {}
	
	protected void systemPreUpdate() {}
	protected void systemUpdate() {}
	protected void systemFixedUpdate(float dt) {}
	protected void systemPostUpdate() {}
}
