package com.morph.engine.core.gui;

import com.morph.engine.core.Game;
import com.morph.engine.debug.Console;
import com.morph.engine.events.*;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.ConsoleTextField;
import com.morph.engine.newgui.GUI;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextField;

/**
 * Created on 11/24/2017.
 */
public class ConsoleGUI extends GUI {
    private Console console;
    private int width;
    private int height;

    private ConsoleTextField consoleInput;
    private TextField consoleOutput;

    private final int FONT_SIZE = 21;

    public ConsoleGUI(Game game, Console console, int width, int height) {
        super(game);
        this.console = console;
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() {
        EventDispatcher.INSTANCE.addEventHandler(this);

        Panel consoleBG = new Panel(new Vector2f(0, height - 500), new Vector2f(width, 500), new Color(0, 1, 0, 0.3f), new Texture("textures/solid.png"));
        Panel consoleInputBG = new Panel(new Vector2f(0, height - 520), new Vector2f(width, 20), new Color(0, 0, 1, 0.3f), new Texture("textures/solid.png"));
        consoleInput = new ConsoleTextField(console, "", "C:/Windows/Fonts/FiraCode-Light.ttf", FONT_SIZE, new Color(1, 1, 1), new Vector2f(0, height - 515), -1200);
        consoleOutput = new TextField("Morph 0.5.15 - Console Output\n", "C:/Windows/Fonts/FiraCode-Light.ttf", FONT_SIZE, new Color(1, 1, 1, 0.7f), new Vector2f(10, height - 20), -1200);
        consoleBG.setDepth(-1000);
        consoleInputBG.setDepth(-1000);
        addElement(consoleBG);
        addElement(consoleInputBG);
        addElement(consoleInput);
        addElement(consoleOutput);
    }

    @Override
    public void load() {
        consoleOutput.setText(console.getText());
    }

    @EventListener(KeyEvent.class)
    public void onKeyEvent(KeyEvent e) {
        if (isOpen()) {
            consoleInput.handleGUIKeyEvent(e);
        }
    }

    @EventListener(ConsoleEvent.class)
    public void onConsoleEvent(ConsoleEvent e) {
        if (isOpen()) {
            if (e.getType() == ConsoleEvent.EventType.UPDATE) {
                consoleOutput.addString(console.getLastLine());
            } else if (e.getType() == ConsoleEvent.EventType.CLEAR) {
                consoleOutput.clearText();
                Console.out.println("Cleared console text.");
            }
        }
    }

    @Override
    public void unload() {

    }
}
