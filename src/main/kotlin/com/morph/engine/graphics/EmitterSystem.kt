package com.morph.engine.graphics

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.physics.components.Transform2D

class EmitterSystem(game : Game) : GameSystem(game) {
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Emitter>() && e.hasComponent<Transform2D>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        super.fixedUpdate(e, dt)
        val emitter = e.getComponent<Emitter>()!!
        val t2d = e.getComponent<Transform2D>()!!
        emitter.acc += dt

        // TODO: Consider moving the spawn loop into Emitter.
        while (emitter.acc >= (1f / emitter.spawnRate)) {
            emitter.spawnParticle(game.world)
            emitter.acc -= (1f / emitter.spawnRate)
        }

        emitter.peek()?.let {
            while (emitter.peek().age >= emitter.lifetime) {
                emitter.peek().parent?.apply { game.world.removeEntity(this) }
                emitter.remove()
            }
        }
    }
}