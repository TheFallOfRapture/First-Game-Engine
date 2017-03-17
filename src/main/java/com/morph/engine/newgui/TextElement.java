package com.morph.engine.newgui;

import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.shaders.GUIShader;
import com.morph.engine.graphics.shaders.GUITextShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.RenderDataUtils;
import com.morph.engine.graphics.shaders.BasicTexturedShader;

public class TextElement extends Element {
	private String text;
	private String font;
	private Color color;
	protected int size;

	private Vector2f bottomLeft;
	private Vector2f topRight;

	public TextElement(String text, String font, int size, Color color, Vector2f position, int depth) {
		super(new Transform2D(position.add(new Vector2f(0, 0)), 0, new Vector2f(1, 1)),
				RenderDataUtils.createText(text, font, size, color, new GUITextShader()),
				depth);
		this.text = text;
		this.font = font;
		this.color = color;
		this.size = size;

		this.bottomLeft = getRenderData().getVertices().get(0).getPosition().getXY();
		this.topRight = getRenderData().getVertices().get(getRenderData().getVertices().size() - 2).getPosition().getXY();
	}

	public TextElement(String text, String font, int size, Color color, Vector2f position) {
		this(text, font, size, color, position, 0);
	}

	public TextElement(String text, String font, int size, Vector2f position) {
		this(text, font, size, new Color(0, 0, 0), position);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text  = text;
		// TODO: Reset textures
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
		//TODO: Reset textures
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Vector2f getBottomLeft() {
		return bottomLeft;
	}

	public Vector2f getTopRight() {
		return topRight;
	}

	public static TextElement centerWithin(String text, String font, int size, Color color, Element e) {
		TextElement textObj = new TextElement(text,
				font,
				size,
				color,
				e.getTransform().getPosition().sub(e.getTransform().getScale().mul(new Vector2f(0.5f, 0.5f))),
				e.getDepth() - 1);

		Vector2f textSize = textObj.getTopRight().sub(textObj.getBottomLeft()).abs();

		Vector2f shift = e.getTransform().getScale().sub(textSize).scale(0.5f);
		textObj.getTransform().translate(shift);

		return textObj;
	}

	public static TextElement centerXWithin(String text, String font, int size, Color color, Element e) {
		TextElement textObj = new TextElement(text,
				font,
				size,
				color,
				e.getTransform().getPosition().sub(e.getTransform().getScale().mul(new Vector2f(0.5f, 0.5f))),
				e.getDepth() - 1);

		Vector2f textSize = textObj.getTopRight().sub(textObj.getBottomLeft()).abs();

		Vector2f shift = e.getTransform().getScale().sub(textSize).mul(new Vector2f(0.5f, 0));
		textObj.getTransform().translate(shift);

		return textObj;
	}

	public static TextElement centerYWithin(String text, String font, int size, Color color, Element e) {
		TextElement textObj = new TextElement(text,
				font,
				size,
				color,
				e.getTransform().getPosition().sub(e.getTransform().getScale().mul(new Vector2f(0.5f, 0.5f))),
				e.getDepth() - 1);

		Vector2f textSize = textObj.getTopRight().sub(textObj.getBottomLeft()).abs();

		Vector2f shift = e.getTransform().getScale().sub(textSize).mul(new Vector2f(0, 0.5f));
		textObj.getTransform().translate(shift);

		return textObj;
	}
}
