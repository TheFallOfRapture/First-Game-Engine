package com.fate.game.testing;

import com.fate.engine.core.Game;
import com.fate.engine.core.OpenGame;
import com.fate.engine.graphics.Color;
import com.fate.engine.math.MatrixUtils;

public class TestingGame extends OpenGame {
	private TestEntity e;

	public TestingGame(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Testing Realm", fps, fullscreen);
	}
	
	@Override
	public void initGame() {
		renderingEngine.setClearColor(new Color(1, 0, 0, 0));
		
		e = new TestEntity(this, 50, 50, 50, 50, new Color(0, 1, 0), new BasicShader());
		addEntity(e);
		
		renderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height / 2f, -height / 2f, -width / 2f, width / 2f, -1, 1));
//		renderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -1, 1));
	}

	@Override
	public void fixedGameUpdate(float dt) {
		// TODO Auto-generated method stub
		
	}

	// TODO: Remove if working
//	@Override
//	public void render() {
//		renderingEngine.render(display, entities);
//	}

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
