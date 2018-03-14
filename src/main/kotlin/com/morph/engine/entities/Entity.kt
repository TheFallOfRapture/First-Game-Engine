package com.morph.engine.entities

data class Entity @JvmOverloads constructor(val name: String, val id: Int, var components: List<Component> = listOf()) : Cloneable {
    fun addComponent(c: Component) {
        components += (c)
        c.init()
    }

    fun removeComponent(c: Component) {
        components -= (c)
        c.destroy()
    }

    fun clearComponents(type: Class<out Component>) {
        components.indices.reversed()
                .asSequence()
                .map { components[it] }
                .filter { type.isInstance(it) }
                .forEach { removeComponent(it) }
    }

    fun clearAllComponents() {
        for (i in components.indices.reversed()) {
            removeComponent(components[i])
        }
    }

    inline fun <reified T : Component> getComponent() = components.filterIsInstance<T>().firstOrNull()
    fun <T : Component> getComponent(type: Class<T>) = components.filterIsInstance(type).map(type::cast).firstOrNull()

    inline fun <reified T : Component> hasComponent() = components.filterIsInstance<T>().isNotEmpty()
    fun <T : Component> hasComponent(type: Class<T>) = components.filterIsInstance(type).isNotEmpty()

    fun destroy() {
        clearAllComponents()
    }

    @SafeVarargs
    fun hasComponents(vararg requiredTypes: Class<out Component>) = requiredTypes.all { this.hasComponent(it) }
}
