package com.morph.engine.graphics;

import com.morph.engine.math.Vector2f;

/**
 * Created on 7/30/2017.
 */
public class LoadedCharacter {
    private char charValue;
    private Vector2f[] texCoords;
    private int[] indices;
    private float xAdvance;
    private float[] offsetData;

    public LoadedCharacter(char charValue, Vector2f[] texCoords, int[] indices, float xAdvance, float[] offsetData) {
        this.charValue = charValue;
        this.texCoords = texCoords;
        this.indices = indices;
        this.xAdvance = xAdvance;
        this.offsetData = offsetData;
    }

    public char getCharValue() {
        return charValue;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public int[] getIndices() {
        return indices;
    }

    public float getXAdvance() {
        return xAdvance;
    }

    public float[] getOffsetData() {
        return offsetData;
    }
}
