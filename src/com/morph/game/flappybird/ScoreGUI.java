package com.fate.game.flappybird;

import java.awt.Color;
import java.awt.Graphics;

import com.fate.engine.graphics.gui.GUI;
import com.fate.engine.graphics.gui.GUITextElement;

public class ScoreGUI extends GUI {
	private GUITextElement score;
	private GUITextElement lossText;
	
	public ScoreGUI(FlappyBirdGame game, int layer) {
		super(game, layer);
		score = new GUITextElement("Score: ", 15, 20, 28, "Helvetica");
		lossText = new GUITextElement("Game Over! Press Enter to restart.", 15, 20, 56, "Helvetica", Color.RED);
		lossText.setEnabled(false);
		addElement(score);
		addElement(lossText);
	}
	
	@Override
	public void render(Graphics g) {
		FlappyBirdGame fbGame = (FlappyBirdGame) game;
		
		score.setText("Score: " + fbGame.getScore());
		
		if (fbGame.hasLost() && !lossText.isEnabled())
			lossText.setEnabled(true);
		
		if (!fbGame.hasLost() && lossText.isEnabled())
			lossText.setEnabled(false);
		
		super.render(g);
	}
}
