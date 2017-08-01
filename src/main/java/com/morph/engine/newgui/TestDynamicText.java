package com.morph.engine.newgui;

import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.FontManager;
import com.morph.engine.graphics.LoadedFont;
import com.morph.engine.graphics.components.TextRenderData;
import com.morph.engine.graphics.shaders.GUITextShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created on 7/30/2017.
 */
public class TestDynamicText extends Element {
    private String text;
    private LoadedFont font;
    private Color color;
    protected int size;

    private Vector2f bottomLeft;
    private Vector2f topRight;

    public TestDynamicText(String text, String font, int size, Color color, Vector2f position, int depth) {
        super(new Transform2D(position.add(new Vector2f(0, 0)), 0, new Vector2f((float) size / (float) LoadedFont.SIZE, (float) size / (float) LoadedFont.SIZE)),
                new TextRenderData(new GUITextShader(), text, FontManager.loadFont(font).getFont(), color),
                depth);
        this.text = text;
        this.font = new LoadedFont(font);
        this.color = color;
        this.size = size;

        this.bottomLeft = getRenderData().getVertices().get(0).getPosition().getXY();
        this.topRight = getRenderData().getVertices().get(getRenderData().getVertices().size() - 2).getPosition().getXY();
    }

    public TestDynamicText(String text, String font, int size, Color color, Vector2f position) {
        this(text, font, size, color, position, 0);
    }

    public TestDynamicText(String text, String font, int size, Vector2f position) {
        this(text, font, size, new Color(0, 0, 0), position);
    }

    public String getText() {
        return text;
    }

    public TextRenderData getRenderData() {
        return (TextRenderData) super.getRenderData();
    }

    public void setText(String text) {
        this.text = text;
        // TODO: Reset textures
        getRenderData().setText(text);
    }

    public void addCharacter(char c) {
        this.text += c;
        getRenderData().addCharacter(c);
    }

    public void removeCharacter() {
        if (text.length() > 0) {
            this.text = text.substring(0, text.length() - 1);
            getRenderData().removeCharacter();
        } else System.err.println("Attempt to remove character from empty string");
    }

    public void addString(String s) {
        this.text += s;
        getRenderData().addString(s);
    }

    public LoadedFont getFont() {
        return font;
    }

    public String getFontName() {
        return font.getFontName();
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
}
