package com.morph.engine.core

import com.morph.engine.entities.Entity

/**
 * Created by Fernando on 1/19/2017.
 */
interface IWorld {
    val game: Game
    val entities : List<Entity>

    fun getEntityByName(name: String): Entity? = entities.find { it.name == name }
    fun getEntityByID(id: Int): Entity? = entities.find { it.id == id }

    fun addEntity(e: Entity): Boolean  // TODO: Should this really be required?
    fun removeEntity(e: Entity): Boolean

    fun init()
    fun destroy() {
        entities.forEach { it.destroy() }
    }
}
