package com.morph.engine.input

import org.lwjgl.glfw.GLFW.*

import com.morph.engine.util.Feed
import io.reactivex.Observable

object Keyboard {
    private val keyEventFeed = Feed<StdKeyEvent>()

    // PRESS, REPEAT, RELEASE
    @JvmStatic val standardKeyEvents: Observable<StdKeyEvent> = Observable.create { keyEventFeed.emit(it) }

    // UP, DOWN
    @JvmStatic val binaryKeyEvents: Observable<BinKeyEvent> = Observable.create {
        keyEventFeed.emit(it) { e ->
            when (e.action) {
                KeyPress -> onNext(BinKeyEvent(KeyDown, e.key, e.mods))
                KeyRelease -> onNext(BinKeyEvent(KeyUp, e.key, e.mods))
                KeyRepeat -> {
                }
            }
        }
    }

    fun handleKeyEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = keyEventFeed.onNext(StdKeyEvent(getKeyAction(action), key, mods))

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
