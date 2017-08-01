package com.morph.engine.graphics;

public class FontResource {
	private LoadedFont font;
	private int count;

	public FontResource(LoadedFont font) {
		this.font = font;
	}
	
	@Override
	public void finalize() {
		font.destroy();
	}
	
	public LoadedFont getFont() {
		return font;
	}
	
	public void addReference() {
		count++;
	}
	
	public boolean removeReference() {
		count--;
		return count == 0;
	}
}
