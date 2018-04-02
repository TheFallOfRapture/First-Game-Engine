package com.morph.engine.script

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity

/**
 * Created on 7/5/2017.
 */
class ScriptSystem(game: Game) : GameSystem(game) {

    override fun acceptEntity(e: Entity): Boolean {
        return e.hasComponent(ScriptContainer::class.java)
    }

    override fun initSystem() {

    }

    override fun preUpdate(e: Entity) {
        super.preUpdate(e)

        e.getComponent(ScriptContainer::class.java)?.getBehaviors()?.forEach { it.preUpdate() }
    }

    override fun update(e: Entity) {
        super.update(e)

        e.getComponent(ScriptContainer::class.java)?.getBehaviors()?.forEach { it.update() }
    }

    override fun fixedUpdate(e: Entity, dt: Float) {
        super.fixedUpdate(e, dt)

        e.getComponent(ScriptContainer::class.java)?.getBehaviors()?.forEach { it.fixedUpdate(dt) }
    }

    override fun postUpdate(e: Entity) {
        super.postUpdate(e)

        e.getComponent(ScriptContainer::class.java)?.getBehaviors()?.forEach { it.postUpdate() }
    }
}
