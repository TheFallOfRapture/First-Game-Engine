package com.morph.engine.gui;

import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.RenderDataUtils;
import com.morph.game.shooting.graphics.shaders.BasicTexturedShader;

public class GUIPanel extends GUIContainer {
	protected Vector2f size;
	
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
