package com.morph.engine.entities

data class Entity @JvmOverloads constructor(val name: String, val id: Int, var components: List<Component> = listOf()) : Cloneable {
    fun addComponent(c: Component) : Entity {
        components += (c)
        c.init()

        return this
    }

    fun removeComponent(c: Component) : Entity {
        components -= (c)
        c.destroy()

        return this
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

inline fun <reified T: Component> given(e: Entity, block: (T) -> Unit) = e.getComponent<T>()?.let(block) ?: println("No component found.")
inline fun <reified T: Component> withComponent(e: Entity, block: T.() -> Unit) = e.getComponent<T>()?.apply(block) ?: println("No component found.")
