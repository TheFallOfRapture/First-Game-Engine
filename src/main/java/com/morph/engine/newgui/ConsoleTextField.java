package com.morph.engine.newgui;

import com.morph.engine.input.KeyPress;
import com.morph.engine.input.StdKeyEvent;
import com.morph.engine.script.debug.Console;
import com.morph.engine.graphics.Color;
import com.morph.engine.math.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created on 8/2/2017.
 */
public class ConsoleTextField extends TextField {
    private Console console;

    public ConsoleTextField(String name, Console console, String text, String font, int size, Color color, Vector2f position, int depth) {
        super(name, text, font, size, color, position, depth);
        this.console = console;
    }

    public ConsoleTextField(String name, Console console, String text, String font, int size, Color color, Vector2f position) {
        super(name, text, font, size, color, position);
        this.console = console;
    }

    public ConsoleTextField(String name, Console console, String text, String font, int size, Vector2f position) {
        super(name, text, font, size, position);
        this.console = console;
    }

    public void handleGUIKeyEvent(StdKeyEvent e) {
        if (e.getAction() instanceof KeyPress && e.getKey() == GLFW_KEY_ENTER) processLine();

        super.handleGUIKeyEvent(e);
    }

    private void processLine() {
        console.readIn(text);
        clearText();
    }
}
