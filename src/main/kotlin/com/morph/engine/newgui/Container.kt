package com.morph.engine.newgui

import com.morph.engine.graphics.components.RenderData
import com.morph.engine.physics.components.Transform2D
import java.util.*

/**
 * Created by Fernando on 10/12/2016.
 */
open class Container(
        name: String,
        transform: Transform2D,
        data: RenderData,
        depth: Int = 0
) : Element(name, transform, data, depth) {
    private var children: List<Element> = ArrayList()

    fun getChildren(deep: Boolean): List<Element> {
        if (deep) {
            var result = listOf<Element>()
            children.forEach { c ->
                result += c
                if (c is Container) {
                    result += c.getChildren(true)
                }
            }

            return result
        }

        return children
    }

    fun addElement(e: Element) {
        this.children += e
    }

    fun addElement(vararg e: Element) {
        children += e
    }
}
