package com.morph.engine.core.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.input.KeyPress;
import com.morph.engine.input.Keyboard;
import com.morph.engine.input.StdKeyEvent;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.ConsoleTextField;
import com.morph.engine.newgui.GUI;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextField;
import com.morph.engine.script.debug.Console;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/**
 * Created on 11/24/2017.
 */
public class ConsoleGUI extends GUI {
    private Console console;
    private int width;
    private int height;

    private ConsoleTextField consoleInput;
    private TextField consoleOutput;

    public ConsoleGUI(Game game, Console console, int width, int height) {
        super(game);
        this.console = console;
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() {
        int FONT_SIZE = 21;

        Panel consoleBG = new Panel("consoleBackground", new Vector2f(0, height - 500), new Vector2f(width, 500), new Color(0, 1, 0, 0.3f), new Texture("textures/solid.png"));
        Panel consoleInputBG = new Panel("consoleInput", new Vector2f(0, height - 520), new Vector2f(width, 20), new Color(0, 0, 1, 0.3f), new Texture("textures/solid.png"));
        consoleInput = new ConsoleTextField("consoleInput", console, "", "C:/Windows/Fonts/FiraCode-Retina.ttf", FONT_SIZE, new Color(1, 1, 1), new Vector2f(0, height - 515), -1200);
        consoleOutput = new TextField("consoleOutput", "Morph " + Game.VERSION_STRING + " - Console Output\n", "C:/Windows/Fonts/FiraCode-Retina.ttf", FONT_SIZE, new Color(1, 1, 1, 0.7f), new Vector2f(10, height - 20), -1200);
        consoleBG.setDepth(-1000);
        consoleInputBG.setDepth(-1000);
        addElement(consoleBG);
        addElement(consoleInputBG);
        addElement(consoleInput);
        addElement(consoleOutput);

        Keyboard.getStandardKeyEvents().subscribe(this::onKeyEvent);

        Console.events()
                .filter(Console.EventType.UPDATE::equals)
                .map(e -> console.getLastLine())
                .filter(Objects::nonNull)
                .subscribe(consoleOutput::addString, e -> System.err.println("Error updating the console."));

        Console.events()
                .filter(Console.EventType.CLEAR::equals)
                .subscribe(e -> onConsoleClear(), e -> System.err.println("Error clearing the console."));
    }

    @Override
    public void load() {
    }

    private void onKeyEvent(StdKeyEvent e) {
        if (isOpen()) {
            consoleInput.handleGUIKeyEvent(e);
        }

        if (e.getAction() == KeyPress.INSTANCE && e.getKey() == GLFW.GLFW_KEY_GRAVE_ACCENT) getGame().toggleConsole();
    }

    private void onConsoleUpdate() {
        System.out.println("Updating the console...");
        consoleOutput.addString(console.getLastLine());
    }

    private void onConsoleClear() {
        System.out.println("Clearing the console...");
        consoleOutput.clearText();
        Console.out.println("Cleared console text.");
    }

    @Override
    public void unload() {

    }
}
