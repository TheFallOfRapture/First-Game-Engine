package com.morph.engine.core.gui;

import com.morph.engine.core.Game;
import com.morph.engine.events.EventDispatcher;
import com.morph.engine.events.EventListener;
import com.morph.engine.events.KeyEvent;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.*;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created by Fernando on 3/6/2017.
 */
public class EngineGUI extends GUI {
    private Button testBtn1;
    private Button testBtn2;
    private TextElement text;
    private TextField textField;

    private int width;
    private int height;

    public EngineGUI(Game game, int gameWidth, int gameHeight) {
        super(game);
        this.width = gameWidth;
        this.height = gameHeight;

        EventDispatcher.INSTANCE.addEventHandler(this);
    }

    @Override
    public void load() {
        float menuBarHeight = 30;
        float toolbarWidth = 250;
        float bottomBarHeight = 150;

        addElement(new TextElement("File", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(5, height - menuBarHeight + 10)));
        addElement(new TextElement("Edit", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(45, height - menuBarHeight + 10)));
        addElement(new TextElement("Source", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(85, height - menuBarHeight + 10)));
        addElement(new TextElement("Refactor", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(145, height - menuBarHeight + 10)));
        addElement(new TextElement("Navigate", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(215, height - menuBarHeight + 10)));
        addElement(new TextElement("Search", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(285, height - menuBarHeight + 10)));
        addElement(new TextElement("Project", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(345, height - menuBarHeight + 10)));
        addElement(new TextElement("Run", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(405, height - menuBarHeight + 10)));
        addElement(new TextElement("Window", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(445, height - menuBarHeight + 10)));
        addElement(new TextElement("Help", "C:/Windows/Fonts/Calibri.ttf", 16, new Vector2f(510, height - menuBarHeight + 10)));
        addElement(new Panel(new Vector2f(0, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x9E9E9E, 1), new Texture("textures/solid.png")));
        addElement(new Panel(new Vector2f(width - toolbarWidth, 0), new Vector2f(toolbarWidth, height - menuBarHeight), new Color(0x9E9E9E, 1), new Texture("textures/solid.png")));
        addElement(new Panel(new Vector2f(toolbarWidth, 0), new Vector2f(width - (toolbarWidth * 2), bottomBarHeight), new Color(0x757575, 1), new Texture("textures/solid.png")));
        addElement(new Panel(new Vector2f(0, height - menuBarHeight), new Vector2f(width, menuBarHeight), new Color(0x616161, 1), new Texture("textures/solid.png")));

        testBtn1 = new Button("Test Button", "C:/Windows/Fonts/Calibri.ttf", 16, new Color(1, 1, 1), new Color(0.5f, 0.5f, 0.5f), new Texture("textures/friendlyBlueMan.png"), new Texture("textures/4Head.png"), new Transform2D(new Vector2f(500, 400), 0, new Vector2f(100, 50)), 0);
        testBtn2 = new Button("Test Button 2", "C:/Windows/Fonts/Calibri.ttf", 16, new Color(1, 1, 1), new Color(0.5f, 0.5f, 0.5f), new Texture("textures/friendlyBlueMan.png"), new Texture("textures/4Head.png"), new Transform2D(new Vector2f(500, 600), 0, new Vector2f(100, 50)), 0);

        testBtn1.setOnHover(() -> System.out.println("Button 1 hovered over!"));
        testBtn2.setOnHover(() -> System.out.println("Button 2 hovered over!"));

        addElement(testBtn1);
        addElement(testBtn2);

        addElement(textField = new TextField("Type here", "C:/Windows/Fonts/Roboto-Regular.ttf", 32, new Color(1, 1, 1), new Vector2f(toolbarWidth + 50, 300)));
    }

    @EventListener(KeyEvent.class)
    public void onKeyEvent(KeyEvent e) {
        textField.handleGUIKeyEvent(e);
    }

    @Override
    public void unload() {

    }
}
