package com.morph.engine.entities

import java.util.ArrayList
import java.util.Arrays
import java.util.stream.Collectors

open class Entity : Cloneable {
    private var components: MutableList<Component>? = null

    val name: String
    val id: Int

    val allComponents: List<Component>?
        get() = components

    constructor() {
        this.components = ArrayList()
    }

    constructor(name: String, id: Int) {
        this.name = name
        this.id = id
        this.components = ArrayList()
    }

    fun addComponent(c: Component) {
        components!!.add(c)
        c.init()
    }

    fun removeComponent(c: Component) {
        components!!.remove(c)
        c.destroy()
    }

    fun clearComponents(type: Class<out Component>) {
        for (i in components!!.indices.reversed()) {
            val c = components!![i]
            if (type.isInstance(c))
                removeComponent(c)
        }
    }

    fun clearAllComponents() {
        for (i in components!!.indices.reversed()) {
            removeComponent(components!![i])
        }
    }

    fun <T : Component> getComponent(type: Class<T>): T? {
        return components!!.stream().filter(Predicate<Component> { type.isInstance(it) }).map<T>(Function<Component, T> { type.cast(it) }).findFirst().orElse(null)
    }

    fun <T : Component> getComponents(type: Class<T>): List<T> {
        return components!!.stream().filter(Predicate<Component> { type.isInstance(it) }).map<T>(Function<Component, T> { type.cast(it) }).collect<ArrayList<T>, Any>(Collectors.toCollection(Supplier<ArrayList<T>> { ArrayList() }))
    }

    fun <T : Component> hasComponent(type: Class<T>): Boolean {
        return components!!.stream().filter(Predicate<Component> { type.isInstance(it) }).collect<ArrayList<Component>, Any>(Collectors.toCollection(Supplier<ArrayList<Component>> { ArrayList() })).size != 0
    }

    fun destroy() {
        clearAllComponents()
    }

    @SafeVarargs
    fun hasComponents(vararg requiredTypes: Class<out Component>): Boolean {
        return Arrays.stream(requiredTypes).allMatch(Predicate<Class<out Component>> { this.hasComponent(it) })
    }
}
