package com.fate.game.shooting;

import com.fate.engine.core.GameApplication;

public class ShootingGameLauncher {
	private static GameApplication gameLauncher;
	private static ShootingGame game;
	
	public static void main(String[] args) {
		game = new ShootingGame(800, 600, 60, false);
		gameLauncher = new GameApplication(game);
		
		gameLauncher.launch();
	}

	/**
	 * TODO: Prototype 1
	 * 
	 * Player
	 * Level (bag of Obstacles)
	 * KeyboardController
	 */
}
