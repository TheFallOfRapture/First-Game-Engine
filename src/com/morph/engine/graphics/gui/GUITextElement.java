package com.morph.engine.graphics.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

@Deprecated
public class GUITextElement extends GUIElement {
	private String text, font;
	private int size, x, y;
	private Color color;
	
	public GUITextElement(int layer, String text, int size, int x, int y, String font, Color color) {
		super(layer);
		this.text = text;
		this.size = size;
		this.x = x;
		this.y = y;
		this.font = font;
		this.color = color;
		
		this.parent = null;
	}
	
	public GUITextElement(int layer, String text, int size, int x, int y, String font) {
		this(layer, text, size, x, y, font, Color.BLACK);
	}
	
	public GUITextElement(String text, int size, int x, int y, String font, Color color) {
		this(0, text, size, x, y, font, color);
	}
	
	public GUITextElement(String text, int size, int x, int y, String font) {
		this(0, text, size, x, y, font, Color.BLACK);
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setFont(new Font(font, Font.PLAIN, size));
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(hints);
		g2d.setColor(color);
		g2d.drawString(text, x, y);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setFontSize(int size) {
		this.size = size;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setFont(String font) {
		this.font = font;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
