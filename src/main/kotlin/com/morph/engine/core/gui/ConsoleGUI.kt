package com.morph.engine.core.gui

import com.morph.engine.core.Game
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.input.KeyPress
import com.morph.engine.input.Keyboard
import com.morph.engine.input.StdKeyEvent
import com.morph.engine.math.Vector2f
import com.morph.engine.newgui.ConsoleTextField
import com.morph.engine.newgui.GUI
import com.morph.engine.newgui.Panel
import com.morph.engine.newgui.TextField
import com.morph.engine.script.debug.Console
import org.lwjgl.glfw.GLFW
import java.util.*

/**
 * Created on 11/24/2017.
 */
class ConsoleGUI(game: Game, private val console: Console, private val width: Int, private val height: Int) : GUI(game) {

    private lateinit var consoleInput: ConsoleTextField
    private lateinit var consoleOutput: TextField

    override fun init() {
        val fontSize = 21

        val consoleBG = Panel("consoleBackground", Vector2f(0f, (height - 500).toFloat()), Vector2f(width.toFloat(), 500f), Color(0f, 1f, 0f, 0.3f), Texture("textures/solid.png"))
        val consoleInputBG = Panel("consoleInput", Vector2f(0f, (height - 520).toFloat()), Vector2f(width.toFloat(), 20f), Color(0f, 0f, 1f, 0.3f), Texture("textures/solid.png"))
        consoleInput = ConsoleTextField("consoleInput", console, "", "C:/Windows/Fonts/FiraCode-Retina.ttf", fontSize, Color(1f, 1f, 1f), Vector2f(0f, (height - 515).toFloat()), -1200)
        consoleOutput = TextField("consoleOutput", "Morph " + Game.VERSION_STRING + " - Console Output\n", "C:/Windows/Fonts/FiraCode-Retina.ttf", fontSize, Color(1f, 1f, 1f, 0.7f), Vector2f(10f, (height - 20).toFloat()), -1200)
        consoleBG.depth = -1000
        consoleInputBG.depth = -1000
        addElement(consoleBG)
        addElement(consoleInputBG)
        addElement(consoleInput)
        addElement(consoleOutput)

        Keyboard.standardKeyEvents.subscribe { this.onKeyEvent(it) }

        Console.events()
                .filter { Console.EventType.UPDATE == it }
                .map { console.lastLine }
                .filter { Objects.nonNull(it) }
                .subscribe ({ consoleOutput.addString(it) }) { System.err.println("Error updating the console.") }


        Console.events()
                .filter { Console.EventType.CLEAR == it }
                .subscribe({ onConsoleClear() }) { System.err.println("Error clearing the console.") }
    }

    override fun load() {}

    private fun onKeyEvent(e: StdKeyEvent) {
        if (isOpen) {
            consoleInput.handleGUIKeyEvent(e)
        }

        if (e.action === KeyPress && e.key == GLFW.GLFW_KEY_GRAVE_ACCENT) game.toggleConsole()
    }

    private fun onConsoleUpdate() {
        println("Updating the console...")
        consoleOutput.addString(console.lastLine)
    }

    private fun onConsoleClear() {
        println("Clearing the console...")
        consoleOutput.clearText()
        Console.out.println("Cleared console text.")
    }

    override fun unload() {

    }
}
