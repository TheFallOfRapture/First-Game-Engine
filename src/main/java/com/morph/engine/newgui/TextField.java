package com.morph.engine.newgui;

import com.morph.engine.events.KeyEvent;
import com.morph.engine.graphics.Color;
import com.morph.engine.input.Keyboard;
import com.morph.engine.math.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created on 8/2/2017.
 */
public class TextField extends TextElement {
    private static HashMap<Integer, Integer> shiftMap = new HashMap<>();

    static {
        int[] keys = {39, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 61, 91, 92, 93, 96};
        List<Integer> keyList = IntStream.concat(IntStream.of(keys), IntStream.rangeClosed(97, 122)).boxed().collect(Collectors.toList());

        int[] shiftKeys = {34, 60, 95, 62, 63, 41, 33, 64, 35, 36, 37, 94, 38, 42, 40, 58, 43, 123, 124, 125, 126};
        List<Integer> shiftKeyList = IntStream.concat(IntStream.of(shiftKeys), IntStream.rangeClosed(65, 90)).boxed().collect(Collectors.toList());

        IntStream.range(0, keyList.size()).forEach(i -> shiftMap.put(keyList.get(i), shiftKeyList.get(i)));
    }

    public TextField(String text, String font, int size, Color color, Vector2f position, int depth) {
        super(text, font, size, color, position, depth);
    }

    public TextField(String text, String font, int size, Color color, Vector2f position) {
        this(text, font, size, color, position, 0);
    }

    public TextField(String text, String font, int size, Vector2f position) {
        this(text, font, size, new Color(0, 0, 0), position, 0);
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

    public void clearText() {
        getRenderData().clearText();
        this.text = "";
    }

    public void addString(String s) {
        this.text += s;
        getRenderData().addString(s);
    }

    public void handleGUIKeyEvent(Keyboard.StdKeyEvent e) {
        switch (e.getAction()) {
            case PRESS:
                if (e.getKey() == GLFW_KEY_BACKSPACE) removeCharacter();
                else if (e.getKey() == GLFW_KEY_ESCAPE) clearText();
                else if (!isIllegalCharacter(e.getKey())) addCharacter(getCharFromKeyData(e.getKey(), e.hasMod(GLFW_MOD_SHIFT)));
                break;
            case REPEAT:
                if (e.getKey() == GLFW_KEY_BACKSPACE) removeCharacter();
                else if (!isIllegalCharacter(e.getKey())) addCharacter(getCharFromKeyData(e.getKey(), e.hasMod(GLFW_MOD_SHIFT)));
        }
    }

    private boolean isIllegalCharacter(int c) {
        return c == GLFW_KEY_LEFT_SHIFT
                || c == GLFW_KEY_RIGHT_SHIFT;
    }

    /*
    39 34
    44 60
    45 95
    46 62
    47 63
    48 41
    49 33
    50 64
    51 35
    52 36
    53 37
    54 94
    55 38
    56 42
    57 40
    59 58
    61 43
    91 123
    92 124
    93 125
    96 126
    97-122 map to uppercase
     */
    protected char getCharFromKeyData(int keycode, boolean shift) {
        int key = keycode;

        boolean isAlphabetical = keycode >= 65 && keycode <= 90;

        if (isAlphabetical) key += 32;

//        System.out.println(keycode + " | " + key + ", " + shiftMap.get(key));

        if (shift) {
            if (shiftMap.get(key) != null) return (char) shiftMap.get(key).intValue();
        }

        return (char) key;
    }
}
