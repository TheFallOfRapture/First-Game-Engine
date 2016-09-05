package com.fate.engine.gui;

import com.fate.engine.math.Vector2f;

import org.lwjgl.stb.STBTruetype;

public class GUITextElement extends GUIElement {
	public GUITextElement(String text, String font, int size, Vector2f position) {
		super(position);
	}
}
