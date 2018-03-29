package com.morph.engine.newgui

import com.morph.engine.graphics.components.RenderData
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D
import com.morph.engine.script.debug.Console
import com.morph.engine.util.State
import com.morph.engine.util.StateMachine
import kotlin.properties.Delegates

/**
 * Created by Fernando on 10/12/2016.
 */
abstract class Element(
        val name: String,
        val transform: Transform2D,
        open val renderData: RenderData,
        var depth: Int = 0
) {
    private val enabled: Boolean = false

    private var onIdle: () -> Unit = { Console.out.println("Idle : " + this) }

    private var onClick: () -> Unit by Delegates.observable({ Console.out.println("Click : " + this) }) {
        _, _, newValue -> state.addTransition("*", "CLICK", newValue)
    }

    private var onHover: () -> Unit by Delegates.observable({ Console.out.println("Hover : " + this) }) {
        _, _, newValue -> state.addTransition("*", "HOVER", newValue)
    }

    private val state: StateMachine = StateMachine(State("IDLE"))

    init {
        state.addPossibilities("IDLE", "HOVER", "CLICK")
        state.addTransition("*", "IDLE", onIdle)
        state.addTransition("*", "HOVER", onHover)
        state.addTransition("*", "CLICK", onClick)

        transform.init()
        renderData.init()
    }

    operator fun contains(point: Vector2f): Boolean {
        val p0 = transform.position - (transform.scale * 0.5f)
        val p1 = p0 + (transform.scale)

        return (p0.x < point.x
                && point.x < p1.x
                && p0.y < point.y
                && point.y < p1.y)
    }

    fun onHover(onHover: () -> Unit) {
        this.onHover = onHover
    }

    fun onClick(onClick: () -> Unit) {
        this.onClick = onClick
    }

    fun setState(stateName: String) = state.changeState(stateName)

    fun getState(): String = state.currentStateName
}
