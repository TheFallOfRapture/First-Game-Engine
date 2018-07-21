package com.morph.engine.input

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.lwjgl.glfw.GLFW.*

object Keyboard {
    // PRESS, REPEAT, RELEASE
    private val keyEvents: PublishSubject<StdKeyEvent> = PublishSubject.create()
    @JvmStatic val standardKeyEvents: Observable<StdKeyEvent> = keyEvents

    // UP, DOWN
    @JvmStatic val binaryKeyEvents: Observable<BinKeyEvent> = standardKeyEvents
            .filter { it.action != KeyRepeat }
            .map {
                when (it.action) {
                    KeyPress -> BinKeyEvent(KeyDown, it.key, it.mods)
                    KeyRelease -> BinKeyEvent(KeyUp, it.key, it.mods)
                    KeyRepeat -> BinKeyEvent(KeyDown, it.key, it.mods) // Unreachable branch
                }
            }

    fun handleKeyEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = keyEvents.onNext(StdKeyEvent(getKeyAction(action), key, mods))

    private fun getKeyAction(action: Int): StdKeyAction = when (action) {
        GLFW_PRESS -> KeyPress
        GLFW_REPEAT -> KeyRepeat
        GLFW_RELEASE -> KeyRelease
        else -> throw IllegalArgumentException("Argument is not a valid input action")
    }

    fun queryUpDown(key: Int, action: BinKeyAction): Boolean {
        val e = binaryKeyEvents.filter { event -> event.key == key }.lastElement()
        return !e.isEmpty.blockingGet() && e.blockingGet().action == action
    }

    fun queryUpDownWithMods(key: Int, action: BinKeyAction, vararg mods: Int): Boolean {
        val e = binaryKeyEvents.filter { event -> event.key == key }.lastElement()
        return !e.isEmpty.blockingGet() && e.blockingGet().action == action && mods.all { e.blockingGet().hasMod(it) }
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
