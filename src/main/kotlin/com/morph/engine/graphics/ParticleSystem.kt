package com.morph.engine.graphics

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.graphics.components.Particle
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D

class ParticleSystem(game : Game) : GameSystem(game) {
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Particle>() && e.hasComponent<Transform2D>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        super.fixedUpdate(e, dt)

        val particle = e.getComponent<Particle>()!!
        val t2d = e.getComponent<Transform2D>()!!

        particle.age += dt

        t2d.position -= Vector2f(0f, 0.01f)
    }
}