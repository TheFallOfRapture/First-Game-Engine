package com.morph.engine.newgui;

import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.shaders.TransitionShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.util.RenderDataUtils;
import com.morph.engine.graphics.shaders.BasicTexturedShader;
import com.morph.engine.graphics.Color;
import com.morph.engine.physics.components.Transform2D;

public class Button extends Container {
    private String text;
	private String font;
	protected int size;

	private Texture hoverTexture;
	private Texture clickTexture;
	private Runnable onClick;

    public Button(String text, String font, int size, Color color, Color buttonColor, Texture texture, Texture altTexture, Transform2D transform, int depth) {
        super(transform,
				RenderDataUtils.createSquare(buttonColor, new TransitionShader(), texture, altTexture, 0),
				depth);
		this.text = text;
		this.font = font;
		this.size = size;

		this.hoverTexture = texture;
		this.clickTexture = texture;

		TextElement textObj = new TextElement(text, font, size, color,
				transform.getPosition().sub(transform.getScale().mul(new Vector2f(0.5f, 0.5f))), depth - 1);

		Vector2f textSize = textObj.getTopRight().sub(textObj.getBottomLeft()).abs();

		Vector2f shift = transform.getScale().sub(textSize).scale(0.5f);
		textObj.getTransform().translate(shift);

		this.addElement(textObj);
    }

	public void setOnClick(Runnable onClick) {
		this.onClick = onClick;
	}

	public String getText() {
		return text;
	}

	public String getFont() {
		return font;
	}
}
