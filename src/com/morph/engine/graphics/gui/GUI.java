package com.morph.engine.graphics.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.morph.engine.core.Game;

public abstract class GUI extends GUIElement {
	protected List<GUIElement> elements;
	protected Game game;
	
	public GUI(Game game, int layer) {
		super();
		this.game = game;
		elements = new ArrayList<GUIElement>();
	}
	
	public GUI(Game game) {
		this(game, 0);
	}
	
	public void addElement(GUIElement e) {
		elements.add(e);
		e.setParent(this);
	}
	
	public void render(Graphics g) {
		if (!enabled) return;
		
		elements.sort(null);
		
		for (GUIElement e: elements)
			if (e.isEnabled())
				e.render(g);
	}
	
	public Game getGame() {
		return game;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}
}
