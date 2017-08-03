package com.morph.engine.events;

import com.morph.engine.graphics.GLDisplay;
import com.morph.engine.input.Keyboard;

public class KeyEvent extends Event {
	private int key, action, mods;

	public KeyEvent(Keyboard keyboard, int key, int action, int mods) {
		super(keyboard);
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
