package com.fate.engine.gui;

import java.util.ArrayList;
import java.util.List;

import com.fate.engine.math.Vector2f;

public class GUIContainer extends GUIElement {
	private List<GUIElement> elements;
	
	public GUIContainer(Vector2f position) {
		super(position);
		elements = new ArrayList<GUIElement>();
	}
	
	public void addElement(GUIElement e) {
		elements.add(e);
	}
	
	public void removeElement(GUIElement e) {
		elements.remove(e);
	}

	public List<GUIElement> getElements() {
		return elements;
	}
}
