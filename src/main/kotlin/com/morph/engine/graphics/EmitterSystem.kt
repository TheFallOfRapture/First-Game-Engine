package com.morph.engine.graphics

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.entities.EntityFactory
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.graphics.components.Particle
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D

class EmitterSystem(game : Game) : GameSystem(game) {
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Emitter>() && e.hasComponent<Transform2D>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        super.fixedUpdate(e, dt)
        val emitter = e.getComponent<Emitter>()!!
        val t2d = e.getComponent<Transform2D>()!!
        emitter.acc += dt

        while (emitter.acc >= (1f / emitter.spawnRate)) {
            val particle = Particle(emitter.color, emitter)
            val spread = 1f
            val randomOffset = Vector2f((Math.random() - 0.5).toFloat() * spread, (Math.random() - 0.5).toFloat() * spread)
            val particlePos = t2d.position + randomOffset

            val entity = EntityFactory.getEntity("Particle-${System.nanoTime()}")
                    .addComponent(Transform2D(scale = Vector2f(0.05f, 0.05f)))
                    .addComponent(particle)
            entity.getComponent<Transform2D>()!!.position = particlePos

            game.world.addEntity(entity)
            emitter.add(particle)

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