package com.morph.engine.oldui;

import com.morph.engine.graphics.LoadedFont;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.shaders.GUITintTransitionShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.util.RenderDataUtils;
import com.morph.engine.graphics.Color;
import com.morph.engine.physics.components.Transform2D;

public class Button extends Container {
    private String text;
	private String font;
	protected int size;

    public Button(String name, String text, String font, int size, Color color, Color buttonColor, Texture texture, Texture altTexture, Transform2D transform, int depth) {
        super(name, transform,
				RenderDataUtils.createTintedSquare(buttonColor, new GUITintTransitionShader(), texture, altTexture, 0),
				depth);
		this.text = text;
		this.font = font;
		this.size = size;

		TextElement textObj = new TextElement(name + "-innerText", text, font, size, color,
				transform.getPosition().sub(transform.getScale().mul(new Vector2f(0.5f, 0.5f))), depth - 1);

		float scaleRatio = (float) size / (float) LoadedFont.SIZE;

		float xShift = (transform.getScale().getX() - textObj.getRenderData().getWidth() * scaleRatio) * 0.5f;
		float yShift = transform.getScale().getY() * 0.5f - (textObj.getFont().getAscent() * textObj.getFont().getScale() * 2.0f);

		System.out.println(textObj.getFont().getAscent() * textObj.getFont().getScale() * scaleRatio);

		textObj.getTransform().translate(new Vector2f(xShift, yShift));

		this.addElement(textObj);
    }

	public String getText() {
		return text;
	}

	public String getFont() {
		return font;
	}
}
