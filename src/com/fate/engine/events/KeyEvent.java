package com.fate.engine.events;

import com.fate.engine.graphics.GLDisplay;

public class KeyEvent extends Event {
	private int key, action, mods;

	public KeyEvent(GLDisplay display, int key, int action, int mods) {
		super(display);
		this.key = key;
		this.action = action;
		this.mods = mods;
	}

	public int getKeyCode() {
		return key;
	}

	public int getAction() {
		return action;
	}

	public int getMods() {
		return mods;
	}
}
