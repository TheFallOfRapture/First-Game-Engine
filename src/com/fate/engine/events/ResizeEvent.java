package com.fate.engine.events;

import com.fate.engine.graphics.GLDisplay;

public class ResizeEvent extends Event {
	private int width, height;
	private boolean fullscreen;

	public ResizeEvent(GLDisplay display, int width, int height, boolean fullscreen) {
		super(display);
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
	}
	
	public GLDisplay getSource() {
		return (GLDisplay) source;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}
}
