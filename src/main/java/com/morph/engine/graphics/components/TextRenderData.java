package com.morph.engine.graphics.components;

import com.morph.engine.graphics.*;
import com.morph.engine.graphics.shaders.Shader;
import com.morph.engine.math.MathUtils;
import com.morph.engine.math.Vector2f;

import java.nio.CharBuffer;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Created on 7/30/2017.
 */
public class TextRenderData extends RenderData {
    private LoadedFont font;
    private String text = "";
    private Vector2f cursorPosition;
    private int previousPointLength;
    private int charCursorPosition;
    private float width, height;
    private int numLines;

    public TextRenderData(Shader<?> shader, String text, LoadedFont font, Color color) {
        super(shader, font.getTextureAtlas());
        this.cursorPosition = new Vector2f(0, 0);
        this.font = font;
        setTint(color);
        if (!Objects.equals(text, "")) addString(text);
    }

    public void loadCharacter(char c) {
        if (c == '\n') {
            newLine();
            return;
        }

        if (c == '\r') return;

        LoadedCharacter charData = font.getCharacter(c);
        float[] offsetData = charData.getOffsetData();
        Vector2f[] texCoords = charData.getTexCoords();

        float kernAdvance = 0;
        if (text.length() >= 1) kernAdvance = font.kerningLookup((char) text.codePointAt(text.length() - 1), c);

        cursorPosition = cursorPosition.add(new Vector2f(kernAdvance, 0));

        Vector2f offsetMin = new Vector2f(offsetData[0], -offsetData[3]);
        Vector2f offsetMax = new Vector2f(offsetData[2], -offsetData[1]);

        addVertex(cursorPosition.add(offsetMin), texCoords[3]);
        addVertex(cursorPosition.add(new Vector2f(offsetMin.getX(), offsetMax.getY())), texCoords[0]);
        addVertex(cursorPosition.add(offsetMax), texCoords[1]);
        addVertex(cursorPosition.add(new Vector2f(offsetMax.getX(), offsetMin.getY())), texCoords[2]);

        addIndices(IntStream.of(0, 1, 3, 1, 2, 3).map(i -> i + previousPointLength).toArray());

        previousPointLength += 4;
        text += c;
        cursorPosition = cursorPosition.add(new Vector2f(charData.getXAdvance() * font.getScale(), 0));
        charCursorPosition++;

        width += charData.getXAdvance() * font.getScale() + kernAdvance;
        height = numLines <= 1 ? font.getScale() : font.getScale() + font.getYAdvance() * numLines;
    }

    public void addCharacter(char c) {
        updateAll(data -> loadCharacter(c));
    }

    public void newLine() {
        cursorPosition.setX(0);
        cursorPosition.setY(cursorPosition.getY() - (font.getYAdvance() * font.getScale()));
        text += "\n";
    }

    public void loadString(String text) {
        CharBuffer.wrap(text.toCharArray()).chars().mapToObj(c -> (char) c).forEach(this::loadCharacter);
    }

    public void addString(String text) {
        updateAll(data -> loadString(text));
    }

    public void setText(String text) {
        clearText();
        addString(text);
    }

    public void moveCursor(int pos) {
        charCursorPosition = MathUtils.clamp(pos, 0, text.length() - 1);
    }

    public void removeCharacter(int index) {
        if (index >= text.length()) {
            System.err.println("Attempt to remove character beyond string length");
            return;
        }

        updateIndices(data -> {
            for (int i = 0; i < 6; i++)
                removeIndexAtPosition(index * 6);
        });

        updateVertices(data -> {
            for (int i = 0; i < 4; i++) removeVertexAtPosition(index * 4);
        });

        LoadedCharacter charData = font.getCharacter(text.charAt(index));
        charCursorPosition--;
        previousPointLength -= 4;

        cursorPosition = cursorPosition.sub(new Vector2f(charData.getXAdvance() * font.getScale(), 0));

        this.text = this.text.substring(0, index) + this.text.substring(index + 1);
    }

    public void removeCharacter(char c) {
        updateIndices(data -> {
            if (text.indexOf(c) == -1) System.err.println("Character not present in text");
            else removeCharacter(text.indexOf(c));
        });
    }

    public void removeCharacter() {
        updateIndices(data -> {
            if (text.length() == 0) System.err.println("Text is empty");
            else removeCharacter(text.length() - 1);
        });
    }

    public void clearText() {
        updateAll(data -> {
            data.getVertices().clear();
            data.getIndices().clear();
        });
        this.text = "";
        this.cursorPosition = new Vector2f(0, 0);
        charCursorPosition = 0;
        previousPointLength = 0;
        width = 0;
        height = 0;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
