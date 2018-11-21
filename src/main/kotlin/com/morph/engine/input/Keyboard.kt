package com.morph.engine.input

import org.lwjgl.glfw.GLFW.*

object Keyboard {
    // PRESS, REPEAT, RELEASE
    private val keyEvents: MutableList<StdKeyEvent> = mutableListOf()
    @JvmStatic val standardKeyEvents: MutableList<StdKeyEvent> = keyEvents

    private val keyDowns: Array<Pair<Boolean, Int>> = Array(GLFW_KEY_LAST - GLFW_KEY_SPACE + 1) { false to 0 }

    // UP, DOWN
    @JvmStatic val binaryKeyEvents: List<BinKeyEvent>
        get() = keyDowns
            .mapIndexed { index, (keyDown, mods) ->
                BinKeyEvent(if (keyDown) KeyDown else KeyUp, index + GLFW_KEY_SPACE, mods)
            }

    fun handleKeyEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (key - GLFW_KEY_SPACE >= 0) {
            keyEvents.add(StdKeyEvent(getKeyAction(action), key, mods))
            if (action == GLFW_PRESS) keyDowns[key - GLFW_KEY_SPACE] = true to mods
            if (action == GLFW_RELEASE) keyDowns[key - GLFW_KEY_SPACE] = false to 0
        }
    }

    private fun getKeyAction(action: Int): StdKeyAction = when (action) {
        GLFW_PRESS -> KeyPress
        GLFW_REPEAT -> KeyRepeat
        GLFW_RELEASE -> KeyRelease
        else -> throw IllegalArgumentException("Argument is not a valid input action")
    }

    fun queryUpDown(key: Int, action: BinKeyAction): Boolean {
        val e = binaryKeyEvents.last { event -> event.key == key }
        return e.action == action
    }

    fun queryUpDownWithMods(key: Int, action: BinKeyAction, vararg mods: Int): Boolean {
        val e = binaryKeyEvents.last { event -> event.key == key }
        return e.action == action && mods.all { e.hasMod(it) }
    }

    fun clear() {
        keyEvents.clear()
        standardKeyEvents.clear()
    }

    fun update() {
    }
}


sealed class KeyAction
sealed class StdKeyAction : KeyAction()
object KeyPress : StdKeyAction()
object KeyRepeat : StdKeyAction()
object KeyRelease : StdKeyAction()

sealed class BinKeyAction : KeyAction()
object KeyUp : BinKeyAction()
object KeyDown : BinKeyAction()

enum class KeyActions(val action: KeyAction) {
    PRESS(KeyPress), REPEAT(KeyRepeat), RELEASE(KeyRelease),
    UP(KeyUp), DOWN(KeyDown)
}

sealed class GenericKeyEvent(val key: Int, val mods: Int) {
    fun hasMod(modCheck: Int): Boolean {
        return mods and modCheck != 0
    }
}
class StdKeyEvent(val action: StdKeyAction, key: Int, mods: Int) : GenericKeyEvent(key, mods)
class BinKeyEvent(val action: BinKeyAction, key: Int, mods: Int) : GenericKeyEvent(key, mods)
