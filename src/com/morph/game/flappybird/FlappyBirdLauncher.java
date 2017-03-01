 package com.fate.game.flappybird;

import com.fate.engine.core.GameApplication;

public class FlappyBirdLauncher {
	private static GameApplication gameLauncher;
	private static FlappyBirdGame game;
	
	public static void main(String[] args) {
		game = new FlappyBirdGame(800, 600, 60, false);
		gameLauncher = new GameApplication(game);
		
		gameLauncher.launch();
	} 
}
