package com.fate.engine.graphics;

import static org.lwjgl.opengl.GL20.glDeleteProgram;

public class TextureResource {
	private int ID;
	private int count;
	
	public TextureResource(int ID) {
		this.ID = ID;
	}
	
	@Override
	public void finalize() {
		glDeleteProgram(ID);
	}
	
	public int getID() {
		return ID;
	}
	
	public void addReference() {
		count++;
	}
	
	public boolean removeReference() {
		count--;
		return count == 0;
	}
}
