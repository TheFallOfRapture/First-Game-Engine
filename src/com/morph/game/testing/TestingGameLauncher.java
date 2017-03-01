package com.fate.game.testing;

import com.fate.engine.core.GameApplication;

public class TestingGameLauncher {
	private static GameApplication gameLauncher;
	private static TestingGame game;
	
	public static void main(String[] args) {
		game = new TestingGame(800, 600, 60, false);
		gameLauncher = new GameApplication(game);
		
		gameLauncher.launch();
	}
}
