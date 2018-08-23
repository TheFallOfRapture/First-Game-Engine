package com.morph.demos.test.main.gui

import com.morph.engine.core.Game
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Colors
import com.morph.engine.graphics.Texture
import com.morph.engine.math.Vector2f
import com.morph.engine.newgui.Button
import com.morph.engine.newgui.GUI
import com.morph.engine.newgui.Panel
import com.morph.engine.newgui.TextElement
import com.morph.engine.physics.components.Transform2D

/**
 * Created by Fernando on 3/6/2017.
 */
class EngineGUI(game: Game, private val width: Int, private val height: Int) : GUI(game) {
    private lateinit var testBtn1: Button
    private lateinit var testBtn2: Button

    override fun init() {
        val menuBarHeight = 30f
        val toolbarWidth = 250f
        val bottomBarHeight = 150f

        val font = "fonts/RobotoCondensed-Regular.ttf"

        addElement(TextElement("fileMenu", "File", font, 16, Vector2f(5f, height - menuBarHeight + 10)))
        addElement(TextElement("editMenu", "Edit", font, 16, Vector2f(45f, height - menuBarHeight + 10)))
        addElement(TextElement("srcMenu", "Source", font, 16, Vector2f(85f, height - menuBarHeight + 10)))
        addElement(TextElement("refMenu", "Refactor", font, 16, Vector2f(145f, height - menuBarHeight + 10)))
        addElement(TextElement("navMenu", "Navigate", font, 16, Vector2f(215f, height - menuBarHeight + 10)))
        addElement(TextElement("searchMenu", "Search", font, 16, Vector2f(285f, height - menuBarHeight + 10)))
        addElement(TextElement("projMenu", "Project", font, 16, Vector2f(345f, height - menuBarHeight + 10)))
        addElement(TextElement("runMenu", "Run", font, 16, Vector2f(405f, height - menuBarHeight + 10)))
        addElement(TextElement("windowMenu", "Window", font, 16, Vector2f(445f, height - menuBarHeight + 10)))
        addElement(TextElement("helpMenu", "Help", font, 16, Vector2f(510f, height - menuBarHeight + 10)))
        addElement(Panel("panel1", Vector2f(0f, 0f), Vector2f(toolbarWidth, height - menuBarHeight), Colors.fromRGBHex(0x9E9E9E, 1f), Texture("textures/solid.png")))
        addElement(Panel("panel2", Vector2f(width - toolbarWidth, 0f), Vector2f(toolbarWidth, height - menuBarHeight), Colors.fromRGBHex(0x9E9E9E, 1f), Texture("textures/solid.png")))
        addElement(Panel("panel3", Vector2f(toolbarWidth, 0f), Vector2f(width - toolbarWidth * 2, bottomBarHeight), Colors.fromRGBHex(0x757575, 1f), Texture("textures/solid.png")))
        addElement(Panel("panel4", Vector2f(0f, height - menuBarHeight), Vector2f(width.toFloat(), menuBarHeight), Colors.fromRGBHex(0x616161, 1f), Texture("textures/solid.png")))

        testBtn1 = Button("testBtn1", "Test Button", font, 16, Color(1f, 1f, 1f), Color(0.5f, 0.5f, 0.5f), Texture("textures/friendlyBlueMan.png"), Texture("textures/4Head.png"), Transform2D(Vector2f(500f, 400f), 0f, Vector2f(100f, 50f)), 0)
        testBtn2 = Button("testBtn2", "Test Button 2", font, 16, Color(1f, 1f, 1f), Color(0.5f, 0.5f, 0.5f), Texture("textures/friendlyBlueMan.png"), Texture("textures/4Head.png"), Transform2D(Vector2f(500f, 600f), 0f, Vector2f(100f, 50f)), 0)

        testBtn1.onHover { println("Button 1 hovered over!") }
        testBtn2.onHover { println("Button 2 hovered over!") }

        addElement(testBtn1)
        addElement(testBtn2)
    }

    override fun load() {}

    override fun unload() {}
}
