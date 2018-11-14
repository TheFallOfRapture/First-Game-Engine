package com.morph.engine.input

import com.morph.engine.core.Camera
import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import java.util.*
import java.util.stream.Collectors

object Mouse {
    private val mouseEvents : Queue<StdMouseEvent> = LinkedList()

    @JvmStatic val standardMouseEvents : Queue<StdMouseEvent> = mouseEvents
    @JvmStatic val binaryMouseEvents : Queue<BinMouseEvent> = mouseEvents
            .stream()
            .map {
                when (it.action) {
                    MousePress -> BinMouseEvent(MouseDown, it.button, it.mods)
                    MouseRelease -> BinMouseEvent(MouseUp, it.button, it.mods)
                }
            }.collect(Collectors.toCollection { LinkedList<BinMouseEvent>() })

    var screenMousePosition = Vector2f(); private set
    var worldMousePosition = Vector2f(); private set

    private var screenToWorld: Matrix4f? = null

    fun handleMouseEvent(window: Long, button: Int, action: Int, mods: Int) {
        mouseEvents.add(StdMouseEvent(getAction(action), button, mods))
    }

    private fun getAction(action: Int): StdMouseAction {
        return when (action) {
            GLFW_PRESS -> MousePress
            GLFW_RELEASE -> MouseRelease
            else -> throw IllegalArgumentException("Argument is not a valid mouse action")
        }
    }

    fun setMousePosition(window: Long, v: Vector2f, camera: Camera) {
        val widthBuffer = BufferUtils.createIntBuffer(1)
        val heightBuffer = BufferUtils.createIntBuffer(1)
        glfwGetWindowSize(window, widthBuffer, heightBuffer)

        val width = widthBuffer.get()
        val height = heightBuffer.get()

        val currentScreenPos = Vector2f(v.x, height - v.y)
        screenMousePosition = currentScreenPos

        if (screenToWorld == null) {
            screenToWorld = camera.projectionMatrix.inverse
        }

        val normalizedMousePos = currentScreenPos.div(Vector2f(width / 2f, height / 2f)) - (Vector2f(1f, 1f)) * (Vector2f(1f, -1f))
        worldMousePosition = screenToWorld!! * currentScreenPos
    }

    fun queryUpDown(button: Int, action: BinMouseAction): Boolean {
        val e = binaryMouseEvents.last { event -> event.button == button }
        return e.action == action
    }

    fun clear() {
        mouseEvents.clear()
        standardMouseEvents.clear()
        binaryMouseEvents.clear()
    }
}

sealed class MouseAction
sealed class StdMouseAction : MouseAction()
object MousePress : StdMouseAction()
object MouseRelease : StdMouseAction()

sealed class BinMouseAction : MouseAction()
object MouseUp : BinMouseAction()
object MouseDown : BinMouseAction()

enum class MouseActions(val action: MouseAction) {
    PRESS(MousePress), RELEASE(MouseRelease),
    UP(MouseUp), DOWN(MouseDown)
}

sealed class GenericMouseEvent(val button: Int, val mods: Int) {
    fun hasMod(modCheck: Int): Boolean {
        return mods and modCheck != 0
    }
}
class StdMouseEvent(val action: StdMouseAction, button: Int, mods: Int) : GenericMouseEvent(button, mods)
class BinMouseEvent(val action: BinMouseAction, button: Int, mods: Int) : GenericMouseEvent(button, mods)
