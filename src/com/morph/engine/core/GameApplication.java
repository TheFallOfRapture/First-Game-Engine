package com.fate.engine.core;

public class GameApplication {
	private Thread gameThread;
	
	public GameApplication(Game game) {
		gameThread = new Thread(game, "GameThread");
	}
	
	public void launch() {
		gameThread.start();
	}
}
