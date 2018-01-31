package com.morph.engine.core;

import com.morph.engine.graphics.CustomDisplay;

import javax.swing.*;

@Deprecated
public class GameApplet extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 800, HEIGHT = 600;
	private Thread gameThread;
	private Game game;
	private CustomDisplay displayReference;

	public GameApplet(Game game) {
		super();
		this.game = game;
	}
	
	public void init() {
//		System.out.println("Game initialized.");
		gameThread = new Thread(game);
		setSize(WIDTH, HEIGHT);
	}
	
	public void start() {
//		System.out.println("Game started.");
//		displayReference = game.getDisplay();
		this.getContentPane().add(displayReference);
		displayReference.setVisible(true);
		gameThread.start();
		displayReference.requestFocus();
	}
	
	public void stop() {
//		System.out.println("Game stopped.");
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void destroy() {
//		System.out.println("Game destroyed.");
	}
}
