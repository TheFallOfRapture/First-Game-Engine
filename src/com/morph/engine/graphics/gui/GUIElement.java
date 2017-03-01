package com.morph.engine.graphics.gui;

import java.awt.Graphics;

public abstract class GUIElement implements Comparable<GUIElement> {
	protected boolean enabled;
	protected int layer;
	protected GUI parent;
	
	public GUIElement(int layer) {
		this.enabled = true;
		this.layer = layer;
		
		this.parent = null;
	}
	
	public GUIElement() {
		this(0);
	}
	
	public abstract void render(Graphics g);
	
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
	
	public GUI getParent() {
		return parent;
	}
	
	public void setParent(GUI parent) {
		this.parent = parent;
	}
	
	@Override
	public int compareTo(GUIElement other) {
		return layer - other.getLayer();
	}
}
