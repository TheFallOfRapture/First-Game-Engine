package com.fate.engine.gui;

import com.fate.engine.graphics.Color;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;
import com.fate.engine.util.RenderDataUtils;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;

public class GUITextElement extends GUIElement {
	protected String text;
	protected String font;
	protected int size;

	public GUITextElement(String text, String font, int size, Color color, Vector2f position, int depth) {
		super(position, depth);
		this.text = text;
		this.font = font;
		this.size = size;
		addComponent(RenderDataUtils.createText(text, font, size, color, new BasicTexturedShader()));
		addComponent(new Transform2D(position.add(new Vector2f(0, 0)), 0, new Vector2f(1, 1)));
	}

	public GUITextElement(String text, String font, int size, Color color, Vector2f position) {
		this(text, font, size, color, position, 0);
	}

	public GUITextElement(String text, String font, int size, Vector2f position) {
		this(text, font, size, new Color(0, 0, 0), position);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text  = text;
		// TODO: Reset textures
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
		//TODO: Reset textures
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
