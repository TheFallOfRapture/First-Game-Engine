package com.fate.game.shooting.controller;

import java.util.Stack;

public class KeyCombo {
	private Stack<Integer> keys;
	private float maxInterval;
	private float interval;
	private boolean comboActive;
	
	public KeyCombo(float maxInterval) {
		keys = new Stack<Integer>();
		this.maxInterval = maxInterval;
	}
	
	public KeyCombo(float maxInterval, int... keyEvents) {
		this.maxInterval = maxInterval;
		this.keys = new Stack<Integer>();
		
		for (int k : keyEvents)
			keys.push(k);
	}
	
	public String toString() {
		if (keys.size() == 0)
			return "KeyCombo()";
		
		String result = "KeyCombo(";
		
		for (int i = 0; i < keys.size() - 1; i++)
			result += keys.get(i) + ", ";
		
		result += keys.get(keys.size() - 1) + ")";
		
		return result;
	}
	
	public void pushKeyEvent(int key) {
		keys.push(key);
		comboActive = true;
		interval = 0;
	}
	
	public void clearAllEvents() {
		keys.clear();
		comboActive = false;
		interval = 0;
	}
	
	public void fixedUpdate(float dt) {
		if (comboActive) {
			if (interval >= maxInterval)
				comboActive = false;
			
			interval += dt;
		}
		
		if (!comboActive && interval > 0) {
			clearAllEvents();
		}
	}
	
	public boolean equals(KeyCombo other) {
		if (other instanceof KeyCombo) {
			return this.keys.equals(other.keys);
		}
		
		return false;
	}
}
