package com.morph.engine.newgui;

import com.morph.engine.debug.Console;
import com.morph.engine.events.KeyEvent;
import com.morph.engine.graphics.Color;
import com.morph.engine.input.Keyboard;
import com.morph.engine.math.Vector2f;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created on 8/2/2017.
 */
public class ConsoleTextField extends TextField {
    private Console console;

    public ConsoleTextField(Console console, String text, String font, int size, Color color, Vector2f position, int depth) {
        super(text, font, size, color, position, depth);
        this.console = console;
    }

    public ConsoleTextField(Console console, String text, String font, int size, Color color, Vector2f position) {
        super(text, font, size, color, position);
        this.console = console;
    }

    public ConsoleTextField(Console console, String text, String font, int size, Vector2f position) {
        super(text, font, size, position);
        this.console = console;
    }

    public void handleGUIKeyEvent(KeyEvent e) {
        switch (e.getAction()) {
            case GLFW_PRESS:
                if (e.getKeyCode() == GLFW_KEY_BACKSPACE) removeCharacter();
                else if (e.getKeyCode() == GLFW_KEY_ESCAPE) clearText();
                else if (e.getKeyCode() == GLFW_KEY_ENTER) processLine();
                else if (isAlphanumeric(e.getKeyCode())) addCharacter(getCharFromKeyData(e.getKeyCode(), Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT) || Keyboard.isKeyDown(GLFW_KEY_RIGHT_SHIFT)));
                break;
        }
    }

    private boolean isAlphanumeric(int keycode) {
        return Character.isLetterOrDigit(keycode);
    }

    private void processLine() {
        console.readIn(text);
        clearText();
    }
}
