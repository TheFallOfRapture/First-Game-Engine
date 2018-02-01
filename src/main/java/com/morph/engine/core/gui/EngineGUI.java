package com.morph.engine.core.gui;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Colors;
import com.morph.engine.graphics.Texture;
import com.morph.engine.math.Vector2f;
import com.morph.engine.newgui.Button;
import com.morph.engine.newgui.GUI;
import com.morph.engine.newgui.Panel;
import com.morph.engine.newgui.TextElement;
import com.morph.engine.physics.components.Transform2D;

/**
 * Created by Fernando on 3/6/2017.
 */
public class EngineGUI extends GUI {
    private Button testBtn1;
    private Button testBtn2;

    private int width;
    private int height;

    public EngineGUI(Game game, int gameWidth, int gameHeight) {
        super(game);
        this.width = gameWidth;
        this.height = gameHeight;
    }

    public void init() {
        float menuBarHeight = 30;
        float toolbarWidth = 250;
        float bottomBarHeight = 150;

        String font = "fonts/RobotoCondensed-Regular.ttf";

        addElement(new TextElement("fileMenu", "File", font, 16, new Vector2f(5, height - menuBarHeight + 10)));
        addElement(new TextElement("editMenu", "Edit", font, 16, new Vector2f(45, height - menuBarHeight + 10)));
        addElement(new TextElement("srcMenu", "Source", font, 16, new Vector2f(85, height - menuBarHeight + 10)));
        addElement(new TextElement("refMenu", "Refactor", font, 16, new Vector2f(145, height - menuBarHeight + 10)));
        addElement(new TextElement("navMenu", "Navigate", font, 16, new Vector2f(215, height - menuBarHeight + 10)));
        addElement(new TextElement("searchMenu", "Search", font, 16, new Vector2f(285, height - menuBarHeight + 10)));
        addElement(new TextElement("projMenu", "Project", font, 16, new Vector2f(345, height - menuBarHeight + 10)));
        addElement(new TextElement("runMenu", "Run", font, 16, new Vector2f(405, height - menuBarHeight + 10)));
        addElement(new TextElement("windowMenu", "Window", font, 16, new Vector2f(445, height - menuBarHeight + 10)));
        addElement(new TextElement("helpMenu", "Help", font, 16, new Vector2f(510, height - menuBarHeight + 10)));
        addElement(new Panel("panel1", new Vector2f(0, 0), new Vector2f(toolbarWidth, height - menuBarHeight), Colors.fromRGBHex(0x9E9E9E, 1f), new Texture("textures/solid.png")));
        addElement(new Panel("panel2", new Vector2f(width - toolbarWidth, 0), new Vector2f(toolbarWidth, height - menuBarHeight), Colors.fromRGBHex(0x9E9E9E, 1f), new Texture("textures/solid.png")));
        addElement(new Panel("panel3", new Vector2f(toolbarWidth, 0), new Vector2f(width - (toolbarWidth * 2), bottomBarHeight), Colors.fromRGBHex(0x757575, 1f), new Texture("textures/solid.png")));
        addElement(new Panel("panel4", new Vector2f(0, height - menuBarHeight), new Vector2f(width, menuBarHeight), Colors.fromRGBHex(0x616161, 1f), new Texture("textures/solid.png")));

        testBtn1 = new Button("testBtn1", "Test Button", font, 16, new Color(1, 1, 1), new Color(0.5f, 0.5f, 0.5f), new Texture("textures/friendlyBlueMan.png"), new Texture("textures/4Head.png"), new Transform2D(new Vector2f(500, 400), 0, new Vector2f(100, 50)), 0);
        testBtn2 = new Button("testBtn2", "Test Button 2", font, 16, new Color(1, 1, 1), new Color(0.5f, 0.5f, 0.5f), new Texture("textures/friendlyBlueMan.png"), new Texture("textures/4Head.png"), new Transform2D(new Vector2f(500, 600), 0, new Vector2f(100, 50)), 0);

        testBtn1.setOnHover(() -> System.out.println("Button 1 hovered over!"));
        testBtn2.setOnHover(() -> System.out.println("Button 2 hovered over!"));

        addElement(testBtn1);
        addElement(testBtn2);
    }

    @Override
    public void load() {}

    @Override
    public void unload() {}
}
