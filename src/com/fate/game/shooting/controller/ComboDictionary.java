package com.fate.game.shooting.controller;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ComboDictionary {
	private List<NamedKeyCombo> combos;
	
	public ComboDictionary() {
		this.combos = new ArrayList<NamedKeyCombo>();
		combos.add(new NamedKeyCombo("Left Dash", GLFW_KEY_A, GLFW_KEY_A));
		combos.add(new NamedKeyCombo("Right Dash", GLFW_KEY_D, GLFW_KEY_D));
		combos.add(new NamedKeyCombo("Super Jump", GLFW_KEY_W, GLFW_KEY_W));
	}
	
	public String getComboName(KeyCombo combo) {
		for (NamedKeyCombo c : combos) {
			if (c.equals(combo)) {
				return c.getName();
			}
		}
		
		return "";
	}
}
