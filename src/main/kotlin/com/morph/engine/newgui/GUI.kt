package com.morph.engine.newgui

import com.morph.engine.core.Game
import java.util.*

/**
 * Created by Fernando on 3/6/2017.
 */
abstract class GUI(val game: Game) {
    private val elements: MutableList<Element>
    var isOpen = false
        private set

    init {
        elements = ArrayList()
    }

    abstract fun init()
    abstract fun load()
    abstract fun unload()

    fun addElement(e: Element) {
        elements.add(e)
        if (e is Container) {
            e.getChildren(true).forEach{ this.addElement(it) }
        }
    }

    fun removeElement(e: Element) {
        elements.remove(e)
        if (e is Container) {
            e.getChildren(true).forEach { this.removeElement(it) }
        }
    }

    fun getElementByName(name: String): Element? {
        return elements.find { it.name == name }
    }

    open fun preUpdate() {}
    open fun update() {}
    open fun fixedUpdate(dt: Float) {}
    open fun postUpdate() {}

    fun getElements(): List<Element> {
        return elements
    }

    fun open() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }
}
