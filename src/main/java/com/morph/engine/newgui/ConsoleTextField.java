package com.morph.engine.newgui;

import com.morph.engine.script.debug.Console;
import com.morph.engine.graphics.Color;
import com.morph.engine.input.Keyboard;
import com.morph.engine.math.Vector2f;

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

    public void handleGUIKeyEvent(Keyboard.StdKeyEvent e) {
        switch (e.getAction()) {
            case PRESS:
                if (e.getKey() == GLFW_KEY_BACKSPACE) removeCharacter();
                else if (e.getKey() == GLFW_KEY_ESCAPE) clearText();
                else if (e.getKey() == GLFW_KEY_ENTER) processLine();
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

    private void processLine() {
        console.readIn(text);
        clearText();
    }
}
