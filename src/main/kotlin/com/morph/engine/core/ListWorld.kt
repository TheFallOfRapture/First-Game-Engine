package com.morph.engine.core

import com.morph.engine.entities.Entity
import java.util.*

/**
 * Created by Fernando on 1/19/2017.
 */
abstract class ListWorld(override val game: Game) : IWorld {
    override val entities : MutableList<Entity> = ArrayList()

    override fun addEntity(e: Entity): Boolean {
        game.renderingEngine.register(e)
        return entities.add(e)
    }

    fun addEntities(e: List<Entity>) {
        entities.addAll(e)
        e.forEach { game.renderingEngine.register(it) }
    }

    override fun removeEntity(e: Entity): Boolean {
        game.renderingEngine.unregister(e)
        e.destroy()
        return entities.remove(e)
    }

    fun removeEntities(e: List<Entity>) {
        entities.removeAll(e)
        e.forEach { en ->
            game.renderingEngine.unregister(en)
            en.destroy()
        }
    }
}
