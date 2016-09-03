package com.fate.game.platformer;

import com.fate.engine.core.Game;

public class PlatformerGame extends Game {
	public PlatformerGame(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Platformer Game", fps, fullscreen);
	}
	
	@Override
	public void initGame() {
		
	}

	@Override
	public void fixedGameUpdate(float dt) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		renderingEngine.render(display, entities);
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preGameUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postGameUpdate() {
		// TODO Auto-generated method stub
		
	}

}
