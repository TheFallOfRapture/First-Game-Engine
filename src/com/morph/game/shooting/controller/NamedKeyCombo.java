package com.fate.game.shooting.controller;

public class NamedKeyCombo extends KeyCombo {
	private String name;
	
	public NamedKeyCombo(String name, int... keyEvents) {
		super(0, keyEvents);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
