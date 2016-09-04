package com.fate.engine.gui;

import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.components.RenderDataUtils;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;

public class GUIPanel extends GUIContainer {
	private Vector2f size;
	
	public GUIPanel(Vector2f position, Vector2f size, Color color, Texture texture) {
		super(position);
		this.size = size;
		
		addComponent(RenderDataUtils.createSquare(color, new BasicTexturedShader(), texture));
		addComponent(new Transform2D(position.add(size.scale(0.5f)), size));
	}
	
	public GUIPanel(Vector2f position, Vector2f size, Texture texture) {
		super(position);
		this.size = size;
		
		addComponent(RenderDataUtils.createSquare(new BasicTexturedShader(), texture));
		addComponent(new Transform2D(position.add(size.scale(0.5f)), size));
	}
	
	public Vector2f getSize() {
		return size;
	}
}
