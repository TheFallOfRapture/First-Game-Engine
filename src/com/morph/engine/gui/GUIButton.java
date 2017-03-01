package com.fate.engine.gui;

import com.fate.engine.math.Vector2f;
import com.fate.engine.util.RenderDataUtils;
import com.fate.engine.graphics.shaders.BasicTexturedShader;
import com.fate.engine.graphics.Color;
import com.fate.engine.physics.components.Transform2D;

public class GUIButton extends GUIElement {
    protected String text;
	protected String font;
	protected int size;

    public GUIButton(String text, String font, int fontSize, Color color, Vector2f position, int depth) {
        super(position, depth);
		this.text = text;
		this.font = font;
		this.size = size;
        addComponent(RenderDataUtils.createText(text, font, size, color, new BasicTexturedShader()));
		addComponent(new Transform2D(position.add(new Vector2f(0, 0)), 0, new Vector2f(1, 1)));
    }
}
