package com.morph.engine.newgui

import com.morph.engine.graphics.Color
import com.morph.engine.input.KeyPress
import com.morph.engine.input.StdKeyEvent
import com.morph.engine.math.Vector2f
import com.morph.engine.script.debug.Console
import org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER

/**
 * Created on 8/2/2017.
 */
class ConsoleTextField(
        name: String,
        var console: Console,
        text: String,
        font: String,
        size: Int,
        color: Color = Color(0f, 0f, 0f),
        position: Vector2f,
        depth: Int = 0
) : TextField(name, text, font, size, color, position, depth) {
    override fun handleGUIKeyEvent(e: StdKeyEvent) {
        if (e.action === KeyPress && e.key == GLFW_KEY_ENTER) processLine()
        super.handleGUIKeyEvent(e)
    }

    private fun processLine() {
        console.readIn(text)
        clearText()
    }
}
