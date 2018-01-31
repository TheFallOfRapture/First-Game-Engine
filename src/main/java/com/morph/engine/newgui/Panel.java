package com.morph.engine.newgui;

import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.shaders.GUITintShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.RenderDataUtils;

public class Panel extends Container {
	public Panel(String name, Vector2f position, Vector2f size, Color color, Texture texture) {
		super(name, new Transform2D(position.add(size.scale(0.5f)), size),
				RenderDataUtils.createTintedSquare(color, new GUITintShader(), texture));
	}
	
	public Panel(String name, Vector2f position, Vector2f size, Texture texture) {
		super(name, new Transform2D(position.add(size.scale(0.5f)), size),
				RenderDataUtils.createTintedSquare(new GUITintShader(), texture));
	}
}
